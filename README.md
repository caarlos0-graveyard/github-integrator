# github-integrator [![Build Status](https://travis-ci.org/caarlos0/github-integrator.svg?branch=master)](https://travis-ci.org/caarlos0/github-integrator) [![Coverage Status](https://img.shields.io/coveralls/caarlos0/github-integrator.svg)](https://coveralls.io/r/caarlos0/github-integrator?branch=master) [![Stories in Ready](https://badge.waffle.io/caarlos0/github-integrator.png?label=ready&title=Ready)](https://waffle.io/caarlos0/github-integrator)

Binds GitHub's PullRequests comments to shell executions.

![screenshot](/image.png)

## Usage

- You will have to create a `.properties` file following the example [here][props];
- You will have to create a `.json` file containing the executions configuration,
like [this][executions];

Right now, the application passes the following parameters down to the script:

- `$1`: the pull request origin repository owner login (e.g.: caarlos0);
- `$2`: the pull request origin repository name (e.g.: github-integrator);
- `$3`: the pull request origin branch (e.g.: feature/more-parameters);
- `$4`: the pull request number;
- `$5` to `$n`: the group values from the regex expression.

Scripts should be fast. If your script is slow, it will hold the execution of
other commands until it ends. This will probably be fixed soon.

While we don't have any stable release, you will need to compile the project
using Maven:

```shell
mvn clean install
```

Then, you can execute the app like this:


```shell
INTEGRATOR_CONFIG=my.properties java -jar \
  target/github-integrator-1.0-SNAPSHOT-jar-with-dependencies.jar
```

[props]: /src/test/resources/test.properties
[executions]: /src/test/resources/test.executions.json

## Throughput

[![Throughput Graph](https://graphs.waffle.io/caarlos0/github-integrator/throughput.svg)](https://waffle.io/caarlos0/github-integrator/metrics)

## Contributing

Feel free to try to fix an existing issue or open new ones.
