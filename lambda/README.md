## Book icon manager lambda

This NodeJS based lambda function will manage the book icons S3 bucket. This bucket contains
resized versions of the book images.

## Lambda trigger

The book images bucket events will trigger this lambda function. It will update the icons bucket
based on the triggering event type.

## Deployment package

To deploy this on AWS Lambda, the source code and the `node_modules` folder must be bundled
in a ZIP file. Creating the deployment package (and uploading it to S3) is 
done by the CI/CD pipeline.