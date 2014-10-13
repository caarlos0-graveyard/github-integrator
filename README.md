# github-integrator

Binds GitHub PullRequest comments to shell executions!

## Usage

- You will have to create a `.properties` file following the example [here][props];
- You will have to create a `.json` file containing the executions configuration,
like [this][executions];

Right now, the applications passes the following parameters to the script:

- `$1`: the pull request origin repository owner login (e.g.: caarlos0);
- `$2`: the pull request origin repository name (e.g.: github-integrator);
- `$3`: the pull request number;


You can execute the app like this:

```sh
java -jar github-integrator.jar my.properties
```

[props]: /caarlos0/github-integrator/blob/master/src/test/resources/test.properties
[executions]: /caarlos0/github-integrator/blob/master/src/test/resources/test.executions.json
