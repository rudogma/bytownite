

Один из многочисленных вариантов. Выполнен на скорую руку, но довольно быстрый (хотя и не самый быстрый из имеющихся у меня). Выложен исключительно в ознакомительных целях (по просьбе трудящихся).
Возможно как-нибудь будет дополнен описанием формата (крайне легкий, может быть быстро восстановлен по исходникам), причин по которым он именно такой и слегка заоптимизирован (чтобы приблизить к физическим ограничениям jvm).

Протестировать скорость (синтетика):
```scala
sbt "test:runMain org.rudogma.bytownite.tests.RunBenches"
```