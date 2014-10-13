# github-integrator [![Build Status](https://travis-ci.org/caarlos0/github-integrator.svg?branch=master)](https://travis-ci.org/caarlos0/github-integrator) [![Stories in Ready](https://badge.waffle.io/caarlos0/github-integrator.png?label=ready&title=Ready)](https://waffle.io/caarlos0/github-integrator)

Binds GitHub's PullRequests comments to shell executions.

![screenshot](/image.png)

## Usage

- You will have to create a `.properties` file following the example [here][props];
- You will have to create a `.json` file containing the executions configuration,
like [this][executions];

Right now, the applications passes the following parameters to the script:

- `$1`: the pull request origin repository owner login (e.g.: caarlos0);
- `$2`: the pull request origin repository name (e.g.: github-integrator);
- `$3`: the pull request origin branch (e.g.: feature/more-parameters);
- `$4`: the pull request number.


You can execute the app like this:

```sh
java -jar github-integrator.jar my.properties
```

[props]: /src/test/resources/test.properties
[executions]: /src/test/resources/test.executions.json
