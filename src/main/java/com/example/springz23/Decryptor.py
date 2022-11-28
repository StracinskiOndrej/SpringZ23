#!/usr/bin/python
from cryptography.fernet import Fernet
from sys import argv
import rsa


path = "./"+ argv[1]
file_path = path + "/"+argv[2]

with open(path+"/private_key.pem", "rb") as key_file:
    prk_data = key_file.read()
private_key = rsa.PrivateKey.load_pkcs1(prk_data)

with open(path+'/encrypted_symmetric_key.txt', 'rb') as e:
    e_key = e.read()
    symmetricKey = rsa.decrypt(e_key, private_key)

with open(file_path, "rb") as encrypted_file:
    encrypted = encrypted_file.read()

f = Fernet(symmetricKey)
data = f.decrypt(encrypted).decode('utf-8')

with open(file_path, "wb") as encrypted_file:
    encrypted_file.write(bytearray(data, "utf-8"))
