# ella
A Clojure wrapper to elasticsearch client

TODO:

1. Test Cases

2.  Wrapping Query DSL


EXAMPLE:

```clojure
(def x (->ESClient {:cname "ankur_local" :ip "localhost" } (atom nil)))
(.init x)
(stats x)
(.destroy x)
```


