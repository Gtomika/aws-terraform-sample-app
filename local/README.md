## Run locally

When running locally, dependencies are mocked out using docker. 
Set up the docker compose with CLI or IntelliJ support.

```
docker-compose -f ./local/docker-compose.yml up
```

Finally, the application can be started.
Make sure to start it with the Spring active profile ``dev``
(can be done with IDE, property or environmental variable).

**Warning**: The localstack S3 don't seem to be working well. It returns 500
internal error when trying to upload a file. Book image update is expected
not to work on the local machine because of this.

**Warning**: The EC2 metadata endpoint does not work during local or test, because it's not running on 
an EC2 instance of course. Could be mocked out but not worth the effort.