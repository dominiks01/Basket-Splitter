# Basket Splitter

The Basket Splitter class is responsible for splitting a list of items into delivery sets based on a provided configuration file.

# Installation

For creating a fat JAR, a Gradle task was defined:
```
jar {
  manifest {
      attributes "Main-Class": "org.example.BasketSplitter"
  }

  from {
      configurations.runtimeClasspath.collect { it.isDirectory() ? it : zipTree(it) }
  }
}
```

To build with a fat JAR, execute the following command in the main directory:

```
./gradlew jar
```

To add the dependency in Gradle, use the following:

```
dependencies {
  [...]

  implementation files('<Path>/<To>/<Jar>/Ocado-1.0-SNAPSHOT.jar')
}
```

# Usage
To use the Basket Splitter, you need to follow these steps:

1)  Import the Basket Splitter class into your project.
2)  Create an instance of the Basket Splitter.
3)  Provide the list of items and the configuration file to the Basket Splitter.
4)  Call the appropriate method to split the items into delivery sets.


```java
  BasketSplitter basketSplitter = new BasketSplitter("/absolute/path/to/config/config.json");
  List<String> basket = Arrays.asList("Product_1", "Product_02", "...");

  System.out.println(basketSplitter.split(basket));
```

# How it works
The SetCover problem is NP-hard, meaning there's no known polynomial-time algorithm to solve all instances optimally. However, we can utilize optimization techniques such as Integer Linear Programming (ILP) to address this problem efficiently.

## Reduction SetCover to ILP
We can prove that SetCover could be reduced to ILP with following steps:
* Define variables:

$$
\text{Let } Y = \{ Y_1, Y_2, \ldots, Y_n \} \\ \text{ represent the delivery options, where } i \text{ represents the } i \text{th delivery option. }
$$


$$
\text{Let } X = \{ x_1, x_2, \ldots, x_n \} \\ \text{ represent the basket, where } i \text{ represents the } i \text{th item. }
$$


* Introduce new variables:

$$
z_i =
\begin{cases}
1 & \text{if } Y_i \text{ is part of solution} \\
0 & \text{otherwise} \\
\end{cases}
]
$$

* The condition of coverage:

$$
\forall x_j : \sum_{i : x_j \in Y_i} z_i \geq 1
$$

* Boundary for ILP:

$$
\quad \forall i : 0 \leq z_i \leq 1,
$$

* The condition of deliveries:

$$
\sum_{i=1}^{n} z_i \geq k
\text{ ,where } n \text{ is the number of delivery options and } k \text{ is the maximum number of sets used.}
$$

QED

## MinSetCover

Now when we do know how to reduce SetCover to ILP we can move 
to ```MinSetCover Problem```. 
All we need to do now is to optimize value of

$$
min(\sum_{i=1}^{n} z_i)
$$

With this defined optimization problem, we can employ various optimization algorithms to find an acceptable solution.
For this task used algorithm was `The Two-Phase Simplex Method`

# Dependencies
* ``` implementation 'com.google.code.gson:gson:2.10.1' ```

  A Java serialization/deserialization library to convert Java Objects into JSON and back

* ``` implementation 'org.apache.commons:commons-math3:3.6.1' ```

  The Apache Commons Math project is a library of lightweight, self-contained mathematics and statistics components 

