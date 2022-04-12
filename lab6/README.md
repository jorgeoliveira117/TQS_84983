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

## 6.4

Senha UA - University Department's ticket management system.

Allows students/teachers to reserve tickets.

Allows workers to "call" the next ticket, close and open departments.

### a)

Chosen quality gate:

| Metric                  | Value | Reason                                                                                          |
|-------------------------|-------|-------------------------------------------------------------------------------------------------|
| Critical Issues         | \>1   | The system has to be working flawlessly.	                                                       |
| Duplicated Blocks       | \>5   | There was a lot of unnecessary duplicated code while developing.	                               |
| Duplicated Lines (%)    | \>10% | Same as above	                                                                                  |
| Maintainability Rating  | <A    | The project should be easy to maintain as it would be used in the future	                       |
| Vulnerabilities         | >0    | Authentication was necessary. Also, no one should be able to tamper with the ticketing system.	 |

### b)



| Issue             | Problem description                                         | How to solve                                                |
|-------------------|-------------------------------------------------------------|-------------------------------------------------------------|
| Code smell (minor) | Several fields have hard to read naming                     | Rename variables to use a lowerCamelCase naming convention  |
| Code smell (major) | Some methods use the same literal string                    | Create a constant and use that instead                      |
| Code smell (major) | Some functions have a high Cognitive Complexity             | Refactor such code using Clean Code principles              |
| Code smell (major) | Several unused fields                                       | Remove them                                                 |
| Bug (major)       | Condition is always false                                   | Change or remove the condition                              |
| Bug (major) | An Optional value is used before knowing if there's content | Add a condition to check if the Optional object has a value |
