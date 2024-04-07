# Basket Splitter
MinSetCover Problem

The `Basket Splitter` class is responsible for splitting a list
of items into delivery sets based on a provided configuration file

# Installation
To install the Basket Splitter, please follow the instructions provided in the Installation Guide.

# Usage
To use the Basket Splitter, you need to follow these steps:

1)  Import the Basket Splitter class into your project.
2)  Create an instance of the Basket Splitter.
3)  Provide the list of items and the configuration file to the Basket Splitter.
4)  Call the appropriate method to split the items into delivery sets.

# How it works
The SetCover problem is NP-hard, meaning there's no known polynomial-time algorithm to solve all instances optimally. However, we can utilize optimization techniques such as Integer Linear Programming (ILP) to address this problem efficiently.

## Reduction to ILP

* $$
\text{Let } Y = \{ Y_1, Y_2, \ldots, Y_n \} \\ \text{ represent the delivery options, where } i \text{ represents the } i \text{th delivery option. }
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

* subject to:

$$
\quad \forall i : 0 \leq z_i \leq 1,
$$

*   The condition of deliveries: 

$$
\sum_{i=1}^{n} z_i \geq k
\text{ ,where } n \text{ is the number of delivery options and } k \text{ is the maximum number of sets used.}
$$

*    Now, the objective is to minimize the value:

$$
min(\sum_{i=1}^{n} z_i)
$$

With this defined optimization problem, we can employ various optimization algorithms to find an acceptable solution