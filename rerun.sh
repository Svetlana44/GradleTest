#!/bin/sh
#указать переменную, адрес файла, из которого брать тесты для запуска

test_file="src/test/resources/FailedTests.txt"
#если файл существует
if [ -f "$test_file" ]; then
  #не пустой файл
  if [ -s "$test_file" ]; then
#можно добавить тэги    ./gradlew clean myTags -x test $(cat $test_file)
    ./gradlew clean test $(cat $test_file)
  else
    echo "test_file is empty"
  fi
fi