const aws = require('@aws-sdk/client-s3');
const sharp = require('sharp');

const s3 = new aws.S3({ apiVersion: '2006-03-01' });

const eventTypeObjectCreated = "s3:ObjectCreated:*";
const eventTypeObjectDeleted = "s3:ObjectRemoved:*";

exports.handler = async (event, context) => {
    console.log('Book images bucket action happened, received event', JSON.stringify(event, null, 2));

    // Get the object from the event
    const eventType = event.Records[0].eventName;
    const bookImagesBucketName = event.Records[0].s3.bucket.name;
    const originalBookImageKey = decodeURIComponent(event.Records[0].s3.object.key.replace(/\+/g, ' '));

    if(eventType === eventTypeObjectCreated) {
        console.log("New book image was uploaded, resizing and uploading icon from it...");
        return await resizeAndUploadIcon(bookImagesBucketName, originalBookImageKey);
    } else if(eventType === eventTypeObjectDeleted) {
        console.log("Book image was deleted, also deleting its icon...");
        return await deleteIcon(bookImagesBucketName, originalBookImageKey);
    } else {
        console.log("Unknown event type, cannot process data: " + eventType);
        throw new Error("Invalid event type: " + eventType);
    }
};

async function resizeAndUploadIcon(bookImagesBucketName, originalBookImageKey) {
    const params = {
        Bucket: bookImagesBucketName,
        Key: originalBookImageKey,
    };
    let originalBookImage;
    try {
        originalBookImage = await s3.getObject(params);
    } catch (err) {
        console.log(`Error getting object ${originalBookImageKey} from bucket ${bookImagesBucketName}`);
        console.log(err);
        throw new Error("Failed to download the object");
    }

    // set thumbnail width. Resize will set the height automatically to maintain aspect ratio.
    const desiredWidth  = 200;

    // Use the sharp module to resize the image and save in a buffer.
    let buffer;
    try {
        buffer = await sharp(originalBookImage.Body).resize(desiredWidth).toBuffer();
    } catch (error) {
        console.log(error);
        console.log("Resizing of image " + originalBookImageKey + " failed.");
        throw new Error("Failed to resize the image");
    }

    const bookIconsBucketName = process.env.BOOK_ICONS_BUCKET_NAME;
    // Upload the thumbnail image to the destination bucket
    try {
        const uploadParams = {
            Bucket: bookIconsBucketName,
            Key: originalBookImageKey,
            Body: buffer,
            ContentType: originalBookImage.ContentType,
            ContentLength: originalBookImage.ContentLength
        };
        await s3.putObject(uploadParams);
    } catch (error) {
        console.log(error);
        console.log('Failed to upload resized image to ' + bookIconsBucketName);
        throw new Error("Failed to upload the object");
    }

    console.log('Successfully resized image ' + originalBookImageKey + ' and uploaded it to ' + bookIconsBucketName);
    return originalBookImageKey;
}

async function deleteIcon(bookImagesBucketName, originalBookImageKey) {
    const bookIconsBucketName = process.env.BOOK_ICONS_BUCKET_NAME;
    const deleteParams = {
      Bucket: bookIconsBucketName,
      Key: originalBookImageKey
    };
    try {
        await s3.deleteObject(deleteParams);
        return originalBookImageKey;
    } catch (error) {
        console.log(error);
        console.log("Failed to delete book icon " + originalBookImageKey + " from bucket " + bookIconsBucketName);
        throw new Error("Failed to delete the object");
    }
}