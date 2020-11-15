# Encrytion algorithms
Algorithms for encryption techniques
##### Work In Progress

### Supported Encryption Techniques
- Monoalphabetic Cipher
- PlayFair Cipher

## Audiofile encryption
### Python

- To encrypt: `python [encryption_technique].py encrypt [audio_file].wav [seed/keyword]`

- To decrypt: `python [encryption_technique].py decrypt [encrypted_file] [seed/keyword]`

### Kotlin

#### Build jar:
`kotlinc [encryption_technique].kt -include-runtime -d [encryption_technique].jar`

- To encrypt: `kotlin [encryption_technique].jar encrypt [audio_file].wav [seed/keyword]`

- To decrypt: `kotlin [encryption_technique].jar decrypt [encrypted_file] [seed/keyword]`
