# Task

Spring boot web
Це ставки на спорт. Юзери ставлять гроші на учасників. В гонці беруть участь авто марок Ferrari, BMW, Audi, Honda.

Має бути 2 ендпоінта:
1. приймає марку та кількість $.
2. Видає кількість $ поставлених на певне авто. Якщо певне авто не вказане - видати інформацію по всім.
   (Бд не потрібна, все в оперативці)

Будь-ласка KISS. Забудьте про ентерпрайз. Пишіть просто, як собі. Не потрібно 100500 классів.  3-5 классів, і всі в одному пакеті - це максимум для задачі.
Без інтерфейсів та успадковування.
Без javax.*
Без GlobalExceptionHandler
Помилки можна просто стрінгою повертати з кодом 200.
Максимально примітивно, проте надійно, потокобезпечно, зрозумлі тести