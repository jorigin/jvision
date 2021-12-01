# Change Log

## [TODO]
### Add
- Wrapper to Vector / Matrix in order to use a vector as a matrix and a matrix as a vector without copying data
- Views on a Vector / Matrix to focus on a specific part of the object without copying the underlying data
- Decomposition `compose()` method to recompute input matrix from a decomposition
- Vector concatenation
- Vector `Vector plusFactor(double, Vector)`
- Vector `Vector plusFactor(double, Vector, Vector)`
- Vector `Vector minusFactor(double, Vector)`
- Vector `Vector minusFactor(double, Vector, Vector)`
- Matrix matrix norms (see (Here)[https://en.wikipedia.org/wiki/Matrix_norm])
- Test implements Point2DTest

### Change
- Refactor Matrix `setTo` to `setValues`
- Refactor Quaternion `mult` into `product`
- Refactor make all xxxAffect() methods to return a reference on the object instead of `void` in order to chain processing
- Refactor make all setValue(s) methods to return a reference on the object for chaining purpose

## [1.0.0](https://github.com/jorigin/jeometry/releases/tag/release-1.0.4)
### Added
### Changed
### Removed

## [1.0.2](https://github.com/jorigin/jeometry/releases/tag/release-1.0.2)
### Added
- Matrix decomposition - Cholesky
- Matrix decomposition - QR
- `Resolvable` interface that describe objects that can solve linear systems
- Matrix horizontal / vertical concatenation methods 
- Matrix `setValues(Matrix)` method that enable to copy the input matrix within the actual 
- Matrix `setTo(double)` method that enable set all the MAtrix values to the given one
- Matrix `extract(int, int, int, int)` and `extract(int, int, int, int, Matrix)` that enable to extract a submatrix
- Vector `setVectorComponent(int, double)` that enables to set all vector component values to the given one
- Vector `setComponents(Vector)` that enables to copy the values from the given vector within the actual
- Vector `setComponents(double[])` that enables to affect all the array values to the corresponding vector components
- Vector `extract(int, int)` that enables to extract a sub-vector from this one 
- Complete test suite for `Resolvable` interface implementations

### Removed
- `decomposeLU()` and `decomposeSVD()` methods from `Matrix` interface

### Changed
- LU related test matrices from `MatTestData`
