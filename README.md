[![Published on Vaadin Directory](https://img.shields.io/badge/Vaadin%20Directory-published-00b4f0?logo=vaadin)](https://vaadin.com/directory/component/vaadin-grid-filter)
[![Latest version](https://img.shields.io/maven-central/v/software.xdev/vaadin-grid-filter?logo=apache%20maven)](https://mvnrepository.com/artifact/software.xdev/vaadin-grid-filter)
[![Build](https://img.shields.io/github/actions/workflow/status/xdev-software/vaadin-grid-filter/check-build.yml?branch=develop)](https://github.com/xdev-software/vaadin-grid-filter/actions/workflows/check-build.yml?query=branch%3Adevelop)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=xdev-software_vaadin-grid-filter&metric=alert_status)](https://sonarcloud.io/dashboard?id=xdev-software_vaadin-grid-filter)
![Vaadin 24+](https://img.shields.io/badge/Vaadin%20Platform/Flow-24+-00b4f0)

# vaadin-grid-filter

A Vaadin Flow component for filtering Grids.

![demo](assets/demo.png)

# Usage

Here is a very simple example how the GridFilter can be used:
```java
Grid<Person> grid = createGrid();

GridFilter<Person> filter = GridFilter.createDefault(grid)
  .withFilterableField("ID", Person::id, Integer.class)
  .withFilterableField("First Name", Person::firstName, String.class);

this.add(filter, grid);
```

To get started further it's recommended to have a look at the [demo](./vaadin-grid-filter-demo).<br/>
A description how to get it running can be found [below](#run-the-demo).

> [!NOTE]
> This component is designed for "in memory" filtering of small to medium sized amounts of data.<br/>
> Filtering multiple thousand items with multiple complex filtering coditions can drastically impact performance and make the UI unresponsive!<br/> In these cases it's recommended to use backend filtering solutions like Database Queries or [ElasticSearch](https://en.wikipedia.org/wiki/Elasticsearch) in combination with a customized UI search framework.<br/>
> If you need help in implementing these feel free to [contact us](#support).

## Installation
[Installation guide for the latest release](https://github.com/xdev-software/vaadin-grid-filter/releases/latest#Installation)

#### Compatibility with Vaadin

| Vaadin version | Grid-Filter version |
| --- | --- |
| Vaadin 24+ (latest) | ``1+`` |

## Run the Demo
* Checkout the repo
* Run ``mvn install && mvn -f vaadin-grid-filter-demo spring-boot:run``
* Open http://localhost:8080

<details>
  <summary>Show example</summary>
  
  ![demo](assets/demo.avif)
</details>

## Support
If you need support as soon as possible and you can't wait for any pull request, feel free to use [our support](https://xdev.software/en/services/support).

## Contributing
See the [contributing guide](./CONTRIBUTING.md) for detailed instructions on how to get started with our project.

## Dependencies and Licenses
View the [license of the current project](LICENSE) or the [summary including all dependencies](https://xdev-software.github.io/vaadin-grid-filter/dependencies)
