# Task Planner

Task Planner is a minimalistic TODO web app built on top of Java Spring Boot.

## Get started

Use these commands to launch the application with the default configuration in `.env.sample`.

> This is not safe for production, before you use it in the real world,
> please change the secrets inside `.env` file and never reveal nor commit them anywhere!

```bash
git clone https://github.com/mat-kubiak/task-planner.git
cd task-planner

# use default .env file
mv .env.sample .env

# build and run containers
docker compose up -d
```


## License

This project is licensed under the GNU General Public License version 3.
You can view the full text of the license in the [LICENSE](./LICENSE) file.
