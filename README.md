# github-integrator [![Build Status](https://travis-ci.org/caarlos0/github-integrator.svg?branch=master)](https://travis-ci.org/caarlos0/github-integrator) [![Coverage Status](https://img.shields.io/coveralls/caarlos0/github-integrator.svg)](https://coveralls.io/r/caarlos0/github-integrator?branch=master) [![Stories in Ready](https://badge.waffle.io/caarlos0/github-integrator.png?label=ready&title=Ready)](https://waffle.io/caarlos0/github-integrator)

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
- `$4`: the pull request number;
- `$5` to `$n`: the group values from the regex expression.


You can execute the app like this:

```shell
INTEGRATOR_CONFIG=config.properties java -jar github-integrator-1.0-SNAPSHOT-jar-with-dependencies.jar
```

[props]: /src/test/resources/test.properties
[executions]: /src/test/resources/test.executions.json

## Throughput

[![Throughput Graph](https://graphs.waffle.io/caarlos0/github-integrator/throughput.svg)](https://waffle.io/caarlos0/github-integrator/metrics)
