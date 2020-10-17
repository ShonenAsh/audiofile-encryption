import random
from string import ascii_letters, digits
from pathlib import Path
import sys, os


class monoalpha_cipher:

    def __init__(self, key):
        random.seed(key)
        chars = list(ascii_letters + digits)
        shuffled = chars.copy()
        random.shuffle(shuffled)
        self.char_map = dict(zip(chars, shuffled))

    def inv(self):
        inv = {v: k for k, v in self.char_map.items()}
        return inv

    def encrypt(self, message, temp_char_map):
        encrypted_message = []
        for letter in message:
            encrypted_message.append(temp_char_map.get(letter, letter))
        return ''.join(encrypted_message)

    def decrypt(self, encrp_str):
        return self.encrypt(encrp_str, self.inv())


def audio_encrypt(filepath, mono_cip):
    # wav file expected
    # if filepath[-4:] != ".wav":
    #     print("Only wav files are supported!")
    #     return

    if Path(filepath).suffix != '.wav':
        print("Only wav files are supported!")
        return

    with open(filepath, "rb") as wav_file:
        data = wav_file.read()

    audio_data = data.hex()
    # encrypt
    encrypted_audio = mono_cip.encrypt(audio_data, mono_cip.char_map)

    output_filename = filepath + ".monocrp"
    with open(output_filename, "w") as file:
        file.write(encrypted_audio)
    print("Encryption complete! \nFile saved as " + output_filename)


def audio_decrypt(filepath: str, mono_cip: monoalpha_cipher):
    # monocrp file expected
    if Path(filepath).suffix != '.monocrp':
        print("Only wav files are supported!")
        return

    with open(filepath, "r") as file:
        data = file.read()

    with open("decrypted_mono.wav", "wb") as audio_file:
        audio_file.write(bytes().fromhex(mono_cip.decrypt(data)))

    print("Decryption complete!")
    print("File saved as decrypted_mono.wav")


if __name__ == '__main__':
    if len(sys.argv) != 4:
        print("Incorrect number of arguments passed!")
        exit(0)

    func_type = sys.argv[1]
    filepath = sys.argv[2]
    if sys.argv[3].isdigit():
        seed = int(sys.argv[3])
    else:
        print("Enter valid seed value!")
        exit(0)
    mc = monoalpha_cipher(seed)

    if os.path.isfile('./' + filepath):
        if func_type == "encrypt":
            audio_encrypt(filepath, mc)
        elif func_type == "decrypt":
            audio_decrypt(filepath, mc)
        else:
            print("Invalid operation! Please refer to the readme file")
            print("Press any key to exit.")
            input()

    else:
        print("File doesn't exist! :(")
        print("Press any key to exit.")
        input()
