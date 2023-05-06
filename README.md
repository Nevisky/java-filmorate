# java-filmorate
Template repository for Filmorate project.

**Промежуточное задание месяца SQL**

[Ссылка на схему БД](https://dbdiagram.io/d/6450250cdca9fb07c453411b)

<details>
<summary>Код БД</summary>

```sql
Table users {
  user_id bigint [primary key]
  user_email varchar(255)
  user_login varchar(20)
  user_birthday timestamp
}

Table films {
  film_id integer [primary key]
  film_name varchar
  film_description varchar(200)
  film_release_date timestamp
  film_duration integer
}

Table film_genre {
  film_id integer  [ref: - films.film_id]
  genre_id integer [ref: > genre.id]
}

Table genre {
  id integer [primary key]
  genre_name varchar(30)
}

Table film_rating {
  film_id integer [ref: - films.film_id]
  film_rating integer [ref: - rating.id]
}

Table rating {
  id integer [primary key]
  name_rating varchar(20)
}

Table status{
  id integer [ref: - friendship.status_id]
  name_status varchar(60)
}

Table friendship{
  user_id integer [ref: - users.user_id]
  friend_id integer [ref: > users.user_id]
  status_id integer 
}

Table films_liked {
  user_id integer [ref: - users.user_id]
  film_id integer [ref: - films.film_id]
}
```

</details>
<picture>
  <source media="(prefers-color-scheme: dark)" srcset="https://github.com/Nevisky/java-filmorate/blob/main/Database%20structure.png">
  <source media="(prefers-color-scheme: light)" srcset="https://github.com/Nevisky/java-filmorate/blob/main/Database%20structure.png">
  <img alt="Shows an illustrated sun in light mode and a moon with stars in dark mode." src="https://github.com/Nevisky/java-filmorate/blob/main/Database%20structure.png">
</picture>
