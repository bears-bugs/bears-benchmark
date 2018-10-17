[![EO principles respected here](http://www.elegantobjects.org/badge.svg)](http://www.elegantobjects.org)
[![DevOps By Rultor.com](http://www.rultor.com/b/smallcreep/cucumber-seeds)](http://www.rultor.com/p/smallcreep/cucumber-seeds)
[![We recommend IntelliJ IDEA](http://www.elegantobjects.org/intellij-idea.svg)](https://www.jetbrains.com/idea/)

[![Build Status](https://travis-ci.org/smallcreep/cucumber-seeds.svg?branch=master)](https://travis-ci.org/smallcreep/cucumber-seeds)
[![PDD status](http://www.0pdd.com/svg?name=smallcreep/cucumber-seeds)](http://www.0pdd.com/p?name=smallcreep/cucumber-seeds)
[![codecov](https://codecov.io/gh/smallcreep/cucumber-seeds/branch/master/graph/badge.svg)](https://codecov.io/gh/smallcreep/cucumber-seeds)
[![License](https://img.shields.io/badge/license-MIT-green.svg)](https://github.com/smallcreep/cucumber-seeds/blob/master/LICENSE.txt)

# cucumber-seeds
Definitions basic steps for automation test on cucumber

**ATTENTION**: We're still in a very early alpha version, the API
may and _will_ change frequently. Please, use it at your own risk,
until we release version 1.0. You can view our progress towards
this release [here](https://github.com/smallcreep/cucumber-seeds/milestone/1).

**Мотивация** Нам не нравятся проекты 
[Beryllium](https://github.com/tomitribe/beryllium), 
[Akita](https://github.com/alfa-laboratory/akita) из-за того, 
что в них не очень правильно называны шаги,
малое количество шагов, отстутвие некоторых функциональностей 
(например: Подключение в одном сценарии к нескольким базам данных).
Отстсутвует возможность не писать кода для новых страниц на UI.

**How to use** TBD

## Примеры шагов

### Шаги для DB

Шаги используют JDBC драйвер.
Зависимость на [jcabi-jdbc](https://github.com/jcabi/jcabi-jdbc)

**Параметры подключения к DB:**
* `cucumber.seeds.db` - алиасы имен DB через запятую, обязательно нужно указать хотябы один,
должны быть уникальными (пр. `master`, `master,input,test`);
* `cucumber.seeds.db.jdbc.driver` -JDBC драйвер (пр. `org.postgresql.Driver`), 
нужно подключать драйвера библиотек, будет использоваться по умолчанию для всех баз,
у которых он не определн;
* `cucumber.seeds.db.%db_name%.jdbc.driver` - JDBC драйвер (пр. `org.postgresql.Driver`), 
нужно подключать драйвера библиотек;
* `cucumber.seeds.db.jdbc.url` - JDBC Url для коннекта к DB
(пр. `jdbc:postgresql://localhost/db_name`), 
будет использоваться по умолчанию для всех баз, у которых он не определн;
* `cucumber.seeds.db.%db_name%.jdbc.url` - JDBC Url для коннекта к DB
(пр. `jdbc:postgresql://localhost/db_name`);
* `cucumber.seeds.db.user` - юзер из под которого будет производится коннект к DB
(пр. `jeff`),
будет использоваться по умолчанию для всех баз, у которых он не определн;
* `cucumber.seeds.db.%db_name%.user` - юзер из под которого будет производится коннект к DB
(пр. `jeff`);
* `cucumber.seeds.db.password` - пароль который будет использоваться при коннект к DB
(пр. `secret`),
будет использоваться по умолчанию для всех баз, у которых он не определн;
* `cucumber.seeds.db.%db_name%.password` - пароль который будет использоваться при коннект к DB
(пр. `secret`);
* `cucumber.seeds.db.encrypted.password` - зашифрованный пароль который будет использоваться при коннект к DB
(пр. `secret`),
будет использоваться по умолчанию для всех баз, у которых он не определн;
* `cucumber.seeds.db.%db_name%.encrypted.password` - зашифрованный пароль который будет использоваться при коннект к DB
(пр. `secret`).

**Шаги для работы с базой данных:**
```gherkin
# Примеры подключения к DB и вставки значений в таблицу
Given The connection to the database master # Подключение к BD с алиасом master.
And There is row in the public.test: # Добавление одной строки в таблицу public.test
  | iid | name  |
  | 1   | first |
And The default schema is public # Устанавливаем схему по умолчанию, можем ее менять в дальнейшем, по умолчанию берется из xml файла схемы базы.
And There are rows in the test: # Добавление нескольких строк в таблицу public.test (используем схему по умолчанию)
  | iid | name   |
  | 2   | second |
  | 3   | third  |
Given The connection to the database input # Подключение к BD с алиасом input.
And There is column login to iid on the public.test2 # Устанавливаем ссылку iid на значение колонки login в таблице public.test2
And The generate function username for column login on the public.test2 # Устанавливаем функцию username для генерации случайного значения в поле login
And There are rows in the public.test2:
  | iid | name   |
  | 1   | first  |
  | 2   | second |
# Получение значений из базы
Given There is value column login from the public.test2 for iid=2 in variable login # Устанавливаем значение в переменную login из значения колонки login таблицы public.test2 для строки с iid=2
And There is count rows from the public.test2 in variable count # Устанавливаем значение в переменную count количества колонок в таблице public.test2
Then The public.test2 should has rows: # Проверка что таблица имеет таки строки, сверяет только указанные значения
  | iid | name   |
  | 1   | first  |
  | 2   | second |
And The public.test2 should has value first in the column name for iid=1 # Проверка что значение в колонке name таблицы public.test2 равно значению first для строки с iid=1  
# TBD Получение значений по условию
# Примеры Изменения значения
# TBD
# Удаление значений
# TBD
# Вызов функций  
# TBD
```

#### iid
По умолчанию `iid` это ссылка на возвращаяемый id строки,
можно переопределить iid к привязке в xml схеме базы данных, там можно использовать составной ключ.
Также можно поменять связь `iid` во время выполнения теста, 
но это можно сделать только один раз для каждой таблицы и до первой вставки в эту таблицу.

#### Generation function

Описать функции для генерации...

Установка функции для генерации может быть произведена в xml описании схемы базы данных.
Также можно переопределять генерацию полей во время теста, 
в этом случае значение будет генерироваться из новой функции до окончания теста 
или нового переопределения в этом тесте.

## How to contribute?
   
Just fork the repo and send us a pull request.

Make sure your branch builds without any warnings/issues:

```
mvn clean install -Pqulice
```

Note: [Checkstyle](https://en.wikipedia.org/wiki/Checkstyle) is used as a static code analyze tool with
[checks list](http://checkstyle.sourceforge.net/checks.html) in GitHub precommits.

## Contributors

 - [@smallcreep](https://github.com/smallcreep) as Ilia Rogozhin
 
## License (MIT)

Copyright (c) 2018 Ilia Rogozhin (@smallcreep) <ilia.rogozhin@gmail.com>

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included
in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
