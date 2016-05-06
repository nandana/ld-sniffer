#! /bin/sh
DATE=$(date +"%Y%m%d%H%M")
rm src/main/resources/database/test/*.*
cp log/execution.log log/$DATE.log

