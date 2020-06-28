## What is the cardinality relationship between EC2 and EBS Volume

1. An EBS volume can be attached to only one EC2 instance in the same AZ
1. Multiple EBS volume can be attached to one EC2 instance in the same AZ


## What are storage types

Block Storage, File storage, Object Storage

## Where does EBS volumes are stored

S3, Snapshots are incremental

## EBS Volumes

* They are only in one AZ
* EBS volume types are - SSD backed and HDD backed
* SSD Types - 3000 IOPS,  3 IO OPS upto 10000, 128MB upto 170GB
* Provisioned 4-16TB
* Can be encrypted (at rest and in-transit)
* Can be elastic
* Can restore volume on larger volume to increase the file-system size
* GP2, IO1, ST1, SC1
* Not suitable for temporary or multi-instance storage (S3 is suitable)
* Amazon EBS encryption uses AWS Key Management Service (AWS KMS) customer master keys when creating encrypted volumes and any snapshots created from your encrypted volumes.


## S3 

* There are 5 storage classes, Standard, OneZone-IA, Standard IA, Intelligent Tiering, Glacier, Glacier-Deep-Archive
* Amazon Glacier is an extremely low-cost storage service that provides secure and durable storage for data archiving and backup. To keep costs low, Amazon Glacier is optimized for data that is infrequently accessed and for which retrieval times of several hours are suitable. The standard retrieval option, which is the default option, takes 3-5 hours to complete. The other options are expedited, which downloads a small amount of data (250 MB maximum) in 5 minutes, and bulk, which downloads large amounts of data (petabytes) in 5-12 hours.
* Stanard - Properties
   * Versioning, 
   * Server acess logging
   * Static website hosting
   * Object-level logging 
   * Default Encryption
* Advanced - Properties
  * Object lock
  * Tags
  * Transfer acceleration
  * Events
  * Requester Pays

* Versioning - Not enabled by default
  * Once we delete, object would have delete marker on it, with GET operation ends up with 404. But older version exist if we have enabled versioning.
* Versioning - suspended
* Server access logging
  * Default disabled (Enable by specifying Target bucket and Target prefix)
  * Requires LOG DELIVERY GROUP write access for the target bucket ACL to log
  * Mangement console adds LOG DELIVERY GROUP by default when loggin is enabled.
  * Access log will only be delivered - SSE-S3 - should be enabled and KMS is not supported
* Static website hosting
  * Region specific end-point - HTTP only and requestor can't pay
  * Should add index document and error document
  * Redirect access to bucket
  * By default blocked to public
  * Should we need to add policy to grant access for public
  * Policy  to access S3 bucket
      ```json
      {
          "Version": "2012-10-17",
          "Statement": [
              {
                  "Sid": "PublicReadGetObject",
                  "Effect": "Allow",
                  "Principal": "*",
                  "Action": [
                      "s3:GetObject"
                  ],
                  "Resource": [
                      "arn:aws:s3:::example.com/*"
                  ]
              }
          ]
      }
      ```
* Object logging - Related to CloudTrail
  * GetObject/DeleteObject/PutObject
  * Can select events that needs to be logged with timestamp
  * We can configure existing trail to log S3 access logging
* Encryption
  * None, AES-256, AWS-KMS
  * SSE-S3 - Server side encryption with S3 managed keys
  * Other encryption
    * SSE-C
    * CSE-KMS
    * CSE-C (customer manged keys)
* Object lock
  * WORM - Write once read many
  * Object lock withou version is not possible
  * We can't disable once we enabled
  * RetentionMode and Governance Mode
  * Retention Period can be enabled
  * Compliance mode would force retention period
  * Governance mode can be disabled that have specific IAM permission
  * Compliance mode cannot be disabled by any user
  * Legal hold - we can't delete even after retention period
* Tags
  * Environment - Production/UAT/DEV
  * S3 Cost Allocation tags
* Transfer Acceleration
  * Amazon CloudFront - is used to transfer acceleration
  * Additional Cost
  * Should be domain accessisible without DOT in bucket name
  * Does not support Get/Put/Delete, 
  * Cross region copies using Put Object Copy
* Events
  * Selected events with Prefix or Suffix
  * SendTo components
    * Lambda, SNS or SQA
* Requester Pays
  * x-amz-requester-pays header is used

## Instance storage

* Temporary and ephemeral
* Reboot retain data, but stopped and terminated would loose data
* It is in the price of instance



## References

* https://cloudacademy.com/blog/how-to-encrypt-an-ebs-volume-the-new-amazon-ebs-encryption/
* Offers very high speed IO