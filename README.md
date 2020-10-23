# Audiofile encrytion
Encryption techniques for audio files

### Supported Encryption Techniques
- Monoalphabetic Cipher

### Usage: Python

#### To encrypt:
`python monoaphabetic_audio.py encrypt [audio_file].wav [seed]`

#### To decrypt:
`python monoaphabetic_audio.py decrypt [encrypted_file].monocrp [seed]`

### Usage: Kotlin

#### Build jar:
`kotlinc monoaphabetic_audio.kt -include-runtime -d monoalpha.jar`

#### To encrypt:
`kotlin monoalpha.jar encrypt [audio_file].wav [seed]`

#### To decrypt:
`kotlin monoalpha.jar decrypt [encrypted_file].monocrp [seed]`
