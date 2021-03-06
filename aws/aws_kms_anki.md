## AWS KMS - overview

* KMS generates and provides a secure central repository of encryption keys
* Used in data encryption (changing data from readable to unreadable)
* Longer the key; the more robus is the encryption
* Easy to secure keys than data, and use stricter and different policy for keys
* S3 server side encryption may use AWS-KMS (SSE-KMS)
* Should be used only for data-at-rest
* AWS Administrators doesn't have access to KMS keys, all os updates involved KMS requires two administrator approval.
* CloudTrails logs realted to KMS is key for investigation
* Region specific service
* KMS can be found under IAM (in management console)



## AWS KMS - encryption to protect data

* Symmetric cryptography
  * Intercept is avoided since KMS is central repository for keys
  * Only decrypt keys are transmitted to decrypt the data
* Key management service
* How key encryption process works
* Different key types
* Manage Key policies
* Key management such as rotate, delete and reinstate keys

## AWS KMS - terms

* Plaintext
* Ciphertext - encrypted, not human readable
* Key - string of characters
* Symetric key algorithm - AES/DES/Triple-DES/Blowfish
* Assymetric key algorithm - RSA, Diffie-Hellman, Digital Signature Alogorithm
* Symmetric crypto is fater than assymetric cryptography
* Do we need both private and public key to decrypt data?
* CMK - Customer master keys
* DEK - Data encryption key
* Envelope encryption
  * Envelope encryption allows store, transfer, and use encrypted data by encapsulating its data keys (DKs) in an envelope
* Key policies - who can use, access keys from KMS (json policy document).
* Grants - Resource based policy, another method to control use & access to AWS KMS (principle + kms:PutKeyPolicy) 

## AWS KMS - Components

* CMK - Main key type
  * Works with upto 4KB
  * CMK used in relation to DEK (Data-encryption key)
  * CMK used to generate DEK
  * AWS Managed CMK
    * For service, by service,  created and used by AWS Service
    * Region specific
  * Customer Managed CMK
    * Rotation, governing access, key policy configuration
    * Enable/disable based on requirement
    * AWS service can be configured to make use of customer-managed-CMK
    * [Protected by FIPS 140-2](https://csrc.nist.gov/projects/cryptographic-module-validation-program/Certificate/3139)
* Three ways of gaining access (to CMK)
  * Key policies, IAM policy and Grants
* By default root-account gains access to CMK using "CMK-A" policy

## AWS KMS - DEK

* Generated by CMK, DEK is immediately encrypted and plain-text DEK is deleted upon data encryption is completed
* Even if hacker has access to encrypted DEK, he can't use it on encrypted data
* Access to CMK is required to decrypt the DEK (even before decryption data)
* AWS KMS discourages encrypting/decrypting data directly with Customer Master Keys (CMKs).

## How Amazon s3 uses KMS for SSE

* [In diagram](https://d2908q01vomqb2.cloudfront.net/22d200f8670dbdb3e253a90eee5098477c95c23d/2020/01/27/IEDC-in-S3-figure-02.png)


## Key policies

* Define key administrators and key users
* PAREC (Principal, Action, Resource, Effect (allow/deny) and Condition )
* Gain access to CMK
  * Key policy (is required for all CMK)
  * Key policies + IAM policy could grant acecss
  * Grants
* IAM Policy alone not sufficient to access CMK (Key policy should allow them to access)
* We must have following entry within the key policy allowing the root full KMS access to the CMK
* Below policy example shows the policy statement that allows access to the AWS account and thereby enables IAM policies.
  ```json
  {
    "Sid": "Enable IAM User Permissions",
    "Effect": "Allow",
    "Principal": {"AWS": "arn:aws:iam::111122223333:root"},
    "Action": "kms:*",
    "Resource": "*"
  }
  ```
* Root user of the account by default has full access to CMK
* Key administrators do not have access to use the CMK, but have access to update the associated key policy.
  * They can delete the key, they can update association and could give access to key access
* Key administrators actions
  * kms:Create, kms:Describe, kms:Enable, kms:List*, kms:Put*, kms:Update, kms:Revoke, kms:Disable, kms:Get*, kms:TagResource, kms:UntagResource
* Key user actions
  * kms:Encrypt, kms:Decrypt, kms:ReEncrypt, kms:GenerateDataKey*, kms:DescribeKey


## Key Grant

* ALlow to delegate identity permission to another AWS principal (within AWS Account)
* Resource based method of access control to the CMK
* GrantId/GrantToken provided by user to another user
* Grant can be provided via AWS KMS API
  * No management console ui for Grants
* Action related to Grants (kms:CreateGrant, kms:ListGrant and kms:RevokeGrant)
* Grantee (receiver of Grant)
  * Some operations for Grantee
  * Decrypt, Encrypt, ReEncryptTo, ReEncryptFrom, GenerateDataKey, GenerateDataKeyWithoutPlaintext, CreateGrant, RetireGrant, DescribeKey
* GrantId and GrantToken are issued to Grantee
* Immediate use of GrantToken with some API would force AWS to replicate faster (workaround for eventual consistency)

## AWS Cli

```bash
aws configure --profile new-user-id-bob
#should provide access-key-id and secret-access-key once prompted
aws kms encrypt --plaintext "This is sample plain text" --key-id alias/DemoKey --profile user-id-bob
## ALice allowing bob to encryp using grant
aws kms create-grant --key-id arn:aws:kms:us-east-1:72384324391066:key/39439-3439493-3434343-343 --grantee-principal arn:aws:iam::73434939439:user/alice --operation "Encrypt" "Decrypt" --profile alice
aws kms encrypt --plaintext "This is sample plain text" --key-id alias/DemoKey --grant-token "verlong-large-token-with-512-chars" --profile user-id-bob
## Update manual key-id
aws kms update-alias --alias-name BobKey --target-key-id 34939-3434-343-3343UUID
```

## Steps for CMK policy (and for new user)

* IAM > Encryption keys > "Create Key" (or Select Key) > "Alias: value, Description: Value and Key-Material-Origin: KMS"
* Add tags
* Define key administrative permissions (choose users/roles) (key adminstrators are not users, they can't use)
* Define key Usage permissions (choose users/roles) (key users can't manage policies)


## AWS Key management

* Rotate key
  * Why to rotate?
    * Reduce security breach, longer the usage probability of breaching is higher
    * Wider blast area when we use same keys
* Automatic rotate key - every 360
  * backing key is changed
  * Older backing key is retained for decrypt older data
  * Won't work for imported keys
  * Aws managed CMKs are rotated every 3 years (1095 days)
* If Old keys are compromised
  * Attacker still can breach the data that was encrypted using it
  * Rotation would help only newer data that used newer keys
* Manual update-alias for manual rotation
* What key material?
  * Backing key
  * Backing key/Key material is not generate when origin is "External"
* Key material can be imported
  * You need public-key (wrapping key)
  * RSAES_OAEP_SHA_256 or RSAES_OAEP_SHA_1 or RSAES_PKCS1_V1_5
  * You can't import plain-text key-material
  * Import-token is generate in this step
  * Use of the import token
    * The import token contains metadata to ensure that your key material is imported correctly. 
    * When we upload encrypted key material to AWS KMS, we must upload the same import token that we downloaded.
  * Import the key-material (it should be in binary format to make use of wrapping key)
  * We can set expiration date
* Deleting CMK
  * Signficance impact to the entire applicaion
  * KMS enforces scheduled deletion process (7-30 days), not imeedite delete. We can cancel the "Cancel key deletion" within this period
  * Would be in pending-deletion (useless state)
  * We can find CloudTrail log for last time usage of this CMK
  * We can setup an alarm to find if anyone using deleted CMK
  * We have option to disable (instead of delete)

  ## Reference

  * [Importing key material step 4: Import the key material](https://docs.aws.amazon.com/kms/latest/developerguide/importing-keys-import-key-material.html)
  * 