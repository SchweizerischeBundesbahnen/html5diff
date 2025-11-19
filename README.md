[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=SchweizerischeBundesbahnen_html5diff&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=SchweizerischeBundesbahnen_html5diff)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=SchweizerischeBundesbahnen_html5diff&metric=bugs)](https://sonarcloud.io/summary/new_code?id=SchweizerischeBundesbahnen_html5diff)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=SchweizerischeBundesbahnen_html5diff&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=SchweizerischeBundesbahnen_html5diff)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=SchweizerischeBundesbahnen_html5diff&metric=coverage)](https://sonarcloud.io/summary/new_code?id=SchweizerischeBundesbahnen_html5diff)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=SchweizerischeBundesbahnen_html5diff&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=SchweizerischeBundesbahnen_html5diff)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=SchweizerischeBundesbahnen_html5diff&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=SchweizerischeBundesbahnen_html5diff)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=SchweizerischeBundesbahnen_html5diff&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=SchweizerischeBundesbahnen_html5diff)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=SchweizerischeBundesbahnen_html5diff&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=SchweizerischeBundesbahnen_html5diff)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=SchweizerischeBundesbahnen_html5diff&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=SchweizerischeBundesbahnen_html5diff)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=SchweizerischeBundesbahnen_html5diff&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=SchweizerischeBundesbahnen_html5diff)

This is a fork of [DaisyDiff in Java project](https://github.com/DaisyDiff/DaisyDiff). The documentation of initial project can be found here: [daisydiff.github.io](https://daisydiff.github.io/).

The aim of this project is not to provide a standalone tool of diffing HTMLs, which DaisyDiff provides among others, but to provide a library of diffing HTMLs for other Java applications.

# Installation

## Adding as a Dependency

Artifacts of this project are uploaded to [Maven Central Repository](https://mvnrepository.com/artifact/ch.sbb.html5diff/html5diff), so to use it as a library in your project, add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>ch.sbb.html5diff</groupId>
    <artifactId>html5diff</artifactId>
    <version>1.4.5</version>
</dependency>
```

# Usage

## Usage as a library

```java
import java.io.StringReader;
import java.io.StringWriter;

import java.util.Locale;

import javax.validation.constraints.NotNull;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import lombok.experimental.UtilityClass;

import org.apache.commons.lang3.tuple.Pair;

import org.outerj.daisy.diff.helper.NekoHtmlParser;
import org.outerj.daisy.diff.html.HTMLDiffer;
import org.outerj.daisy.diff.html.HtmlSaxDiffOutput;
import org.outerj.daisy.diff.html.TextNodeComparator;
import org.outerj.daisy.diff.html.dom.DomTreeBuilder;
        
import org.xml.sax.InputSource;

@UtilityClass
public class DiffToolUtils {
    public String computeDiff(@NotNull Pair<String, String> pairToDiff) {
        try {
            StringWriter finalResult = new StringWriter();
            SAXTransformerFactory tf = (SAXTransformerFactory) TransformerFactory.newInstance();

            TransformerHandler handler = tf.newTransformerHandler();
            handler.getTransformer().setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            handler.getTransformer().setOutputProperty(OutputKeys.INDENT, "no");
            handler.getTransformer().setOutputProperty(OutputKeys.METHOD, "html");
            handler.getTransformer().setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            handler.setResult(new StreamResult(finalResult));

            Locale locale = Locale.getDefault();

            NekoHtmlParser parser = new NekoHtmlParser();

            InputSource oldSource = new InputSource(new StringReader(pairToDiff.getLeft()));
            InputSource newSource = new InputSource(new StringReader(pairToDiff.getRight()));

            DomTreeBuilder oldHandler = new DomTreeBuilder();
            parser.parse(oldSource, oldHandler);
            TextNodeComparator leftComparator = new TextNodeComparator(oldHandler, locale);

            DomTreeBuilder newHandler = new DomTreeBuilder();
            parser.parse(newSource, newHandler);
            TextNodeComparator rightComparator = new TextNodeComparator(newHandler, locale);

            new HTMLDiffer(new HtmlSaxDiffOutput(handler, "diff")).diff(leftComparator, rightComparator);

            return finalResult.toString();
        } catch (Exception ex) {
            return "Error occurred while getting HTML diff";
        }
    }
}
```

## Usage in a CLI

There is a side project [HTML5Diff CLI](https://gitlab.com/opendevise/oss/html5diff-cli) which provides CLI 
interface to functionality of this project. Please, read documentation of that project about how to use it. 


# Project Build

This project is built using Maven. To compile the project, run the following command:
```
$ mvn compile
```

To compile the project and run the tests, use:
```
$ mvn test
```

To compile the project, run the tests, and build the (thin) jar file, use:
```
$ mvn package
```

To compile the project, run the tests, build the (thin) jar file and install it to local Maven repository, use:
```
$ mvn install
```

