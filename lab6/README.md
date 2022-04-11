## 6.2

### f)

Yes, there was 1 bug found and 25 "code smells" which most of them are refactors and replacements from deprecated methods. The bug refers to a Random variable that could be stored as a global variable instead of being created every function call.

### g)


| Issue | Problem description | How to solve |
|---|---|---|
| Bug                   | Save and re-use this "Random" | Change the variable from a local to global variable. This way it's only started once|
| Vulnerability         | Make sure that using this pseudorandom number generator is safe here | Make sure that the variable in question is not used in any kind of security related operation.  |
| Code smell (major)    | Replace this use of System.out or System.err by a logger. | Change from  System.out.println("My Message") -> logger.log("My Message") |
| Code smell (major)    | Remove this unused method parameter "subset" | Implement functionality in the function |

## 6.3

### a)

Debt: 46 min
This debt refers to an approximate time required to fix all bugs, vulnerabilities and code smells.

### d)

| Class | Coverage | Uncovered Lines | Uncovered Conditions |
|---|---|---|---|
| Bug                   | 33.3% | 2 | - |
| Vulnerability         | 53.1%	 | 4 | 11 |
| Code smell (major)    | 80.0%	 | 1 | 2 | 
| Code smell (major)    | 100% | 0 | - |