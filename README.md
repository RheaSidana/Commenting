# Commenting

<p>
Outlines the endpoints for a social media platform, 
offering functionality to add comments and show comments.
 While adding comment: one user, writes a comment on 
another user. While showing comments: the person can view the 
comments they have received from other users.

<br/><b> Assumptions: </b><br/> 
&nbsp; 1. The person writing the comment should always be 
present in the database. 
<br/>&nbsp; 2. The person might or might not be present 
who receives the message.
<br/>&nbsp; 3. Data is seed in the Users Table, for Add 
Comment API. 
</p>

<h1>Installation</h1>

<h3>1. Clone the Repository</h3>

```
git clone https://github.com/RheaSidana/Commenting.git
```

```
cd Commenting
```

<h3>2. Build the project</h3>

```
./gradlew clean build
```

<h3>3. PostgreSQL </h3>
<h4>Edit the username and password details in the "build.gradle" file and "application.yml" file</h4>
</br></br>
<h4>Create the db</h4>

```
create database commenting;
```

<h4>Connect the db</h4>

```
\c commenting;
```

<h3>4. Migrate Tables</h3>

```
./gradlew flywayMigrate
```

<h4>Postgres: check the tables are migrated using: </h4>

```
\dt
```

<h3>5. Run the application: </h3>

```
./gradlew bootRun
```

<h3>6. API's </h3>
<a href="https://documenter.getpostman.com/view/28378586/2s9YXk41xP">Postman Documentation</a>
