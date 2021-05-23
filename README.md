# Pixivia
A personal mirai-based QQ robot for fun.

## Features
 - Image tag prediction
 - Pixiv image query/recommendation/notification push

## Submodule
[ControlNet/pixivia.pixivpy-server](https://github.com/ControlNet/pixivia.pixivpy-server) A Pixiv API http server as backend.

## Requirements
```groovy
implementation "org.jetbrains.kotlin:kotlin-stdlib:1.4.21"
implementation "net.mamoe:mirai-core:2.0-RC"
implementation "net.mamoe:mirai-core-api:2.0-RC"
runtimeOnly "net.mamoe:mirai-login-solver-selenium:latest"
implementation "com.github.doyaaaaaken:kotlin-csv-jvm:0.7.3"
implementation "com.google.code.gson:gson:2.8.6"
```

## Acknowledgements 
 - Inspired from [Cyl18/SeTuDetectorQQBot](https://github.com/Cyl18/SeTuDetectorQQBot)
 - Tag classification model and sources for prediction are from [KichangKim/DeepDanbooru](https://github.com/KichangKim/DeepDanbooru)
 - QQBot API based on [mamoe/mirai](https://github.com/mamoe/mirai)
 - Pixiv API based on [upbit/pixivpy](https://github.com/upbit/pixivpy)
