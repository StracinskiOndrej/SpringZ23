#!/usr/bin/python
from sys import argv
from cryptography.fernet import Fernet
from base64 import b64encode
import rsa

path = argv[1]

file_path = ""+path + "/"+argv[2]

with open(path+"/public_key.pem", "rb") as key_file:
    pk_data = key_file.read()
    public_key = rsa.PublicKey.load_pkcs1(pk_data)

symmetricKey = Fernet.generate_key()
f = Fernet(symmetricKey)
with open(file_path, "rb") as enc_file:
    data = enc_file.read()
enc_data = f.encrypt(data)
b64_enc_data = b64encode(enc_data).decode('utf-8')

enc_symmetricKey = rsa.encrypt(symmetricKey, public_key)
b64_enc_symmetricKey = b64encode(enc_symmetricKey).decode('utf-8')

with open(file_path, "wb") as encrypted_file:
    encrypted_file.write(enc_data)

with open(path + '/encrypted_symmetric_key.txt', 'wb') as e_key:
    e_key.write(enc_symmetricKey)
