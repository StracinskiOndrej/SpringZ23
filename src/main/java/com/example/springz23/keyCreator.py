#!/usr/bin/python
import os
from sys import argv
import rsa


path = argv[1]

publicKey, privateKey = rsa.newkeys(2048)

isExist = os.path.exists(path)
if isExist:
    with open(path+'/public_key.pem', 'wb') as f:
        f.write(publicKey.save_pkcs1('PEM'))
else:
    os.makedirs(path)
    with open(path+'/public_key.pem', 'wb') as f:
        f.write(publicKey.save_pkcs1('PEM'))

isExist = os.path.exists(path)
if isExist:
    with open(path+'/private_key.pem', 'wb') as f:
        f.write(privateKey.save_pkcs1('PEM'))
else:
    os.makedirs(path)
    with open(path+'/private_key.pem', 'wb') as f:
        f.write(privateKey.save_pkcs1('PEM'))