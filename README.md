# CUBLONO Quiescent

  [![Build Status](https://travis-ci.org/s-ted/cublono-quiescent)](https://travis-ci.org/s-ted/cublono-quiescent.svg)
  [![Dependencies Status](http://jarkeeper.com/cubane/cublono-quiescent)](http://jarkeeper.com/cubane/cublono-quiescent/status.svg)
  [![Download Status](https://jarkeeper.com/cubane/cublono-quiescent)](https://jarkeeper.com/cubane/cublono-quiescent/downloads.svg)

  Lisp/Hiccup style templating in [ClojureScript](https://github.com/clojure/clojurescript) producing ReactJS (Quiescent) component.

## Installation

   [![Clojar link](https://clojars.org/cubane/cublono-quiescent/latest-version.svg)](https://clojars.org/cubane/cublono-quiescent)

## Dependencies

   *Cublono-quiescent* depends on:
   - [cublono](https://github.com/s-ted/cublono) for the generic parsing process,
   - [quiescent](https://github.com/levand/quiescent) for the specific ReactJS object generation.


## Usage

```clojure
(ns myns
  (:require [cublono-quiescent.interpreter :as i]))

(i/interpret [:div.my-class#my-id {:attr "value"} "substuff..."])
```

is the same as the quiescent call:

```clojure
(ns myns
 (:require [quiescent.core :as q]
           [quiescent.dom :as d]))

(q/component
  (fn []
    (d/div {:className "my-class"
            :id        "my-id"
            :attr      "value"}
           "substuff...")))
```

See test cases for more examples.

## License

   Copyright © 2016 [Sylvain Tedoldi](https://github.com/s-ted)

   Distributed under the Eclipse Public License either version 1.0 or
   (at your option) any later version.
