(ns cljsql.core
  (:require [instaparse.core :as insta])
  (:import (org.apache.calcite.sql.parser SqlParser)))

(def parse-sql
  (insta/parser
    "SQL                = SELECT_QUERY
     <SELECT_QUERY>     = <SELECT> FIELDS <FROM> <TABLE> WHERE_CLAUSE?  ORDER_CLAUSE?
     SELECT             = 'select '
     FIELDS             = (FIELD)+ | '* '
     <FIELD>            = #'[a-z|_|(|)]+'  <#' '+>
     FROM               = 'from '
     TABLE              = #'[a-z|_|0-9]+' <' '>
     PREDICATE          = (#'[a-z|A-Z|_| |(|)|>|<|=|!|0-9|?|\\'|%]+')
     WHERE              = 'where '
     WHERE_AND          = ' and '
     WHERE_CLAUSE       = <WHERE> PREDICATE {WHERE_AND PREDICATE}
     ORDER_STMT         = 'order by '
     ORDER_CLAUSE       = <ORDER_STMT>  (#'[a-z|A-Z|_| |>|<|=|0-9|?|\\'|%]+')
     "
    :string-ci true))

;antlr
;https://github.com/srikalyc/Sql4D/blob/2c052fe60ead5a16277c798a3440de7d4f6f24f6/Sql4DCompiler/src/main/java/com/yahoo/sql4d/converter/druidG.tokens


(defn get-parser-config []
  (let [config-builder (doto
                         (SqlParser/configBuilder)
                         (.setCaseSensitive false))]
    (.build config-builder)))

(defn parse [stmt]
  (let [parser
        (SqlParser/create stmt (get-parser-config))]
    (.parseStmt parser)
    ))