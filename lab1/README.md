# 2.e

There's a large coverage of euromillions and sets packages (above 80%)
and no coverage for UI tests, which makes sense as UI is tested visually
and not through code.

Not all branches for euromillions and sets packages are being tested,
this means that some tests are not using some of the available functions.
However, the missed functions may not need testing.

Missed functions for...

... SetOfNaturals class:

- hashCode()
- equals(Object)

... CuponEuromillions class:

- format()

... CuponEuromillions class:

- hashCode()
- equals(Object)
- 
... EuromillionsDraw class:

- generateRandomDraw()
- getDrawResults()


