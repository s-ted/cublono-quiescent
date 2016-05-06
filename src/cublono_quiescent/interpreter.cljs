;   Copyright (c)  Sylvain Tedoldi. All rights reserved.
;   The use and distribution terms for this software are covered by the
;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;   which can be found in the file epl-v10.html at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.
;

(ns cublono-quiescent.interpreter
  (:require
    [cublono.normalize :as normalize]
    [cublono.util :as util]
    [clojure.string :as str]
    [quiescent.factory :as f]
    [quiescent.dom :as d]))

(defprotocol Interpretable
  (interpret [this] "Interpret a Clojure data structure as a Queiscient structure"))



(def ^:private tag->component
  {:a                          d/a
   :abbr                       d/abbr
   :address                    d/address
   :area                       d/area
   :article                    d/article
   :aside                      d/aside
   :audio                      d/audio
   :b                          d/b
   :base                       d/base
   :bdi                        d/bdi
   :bdo                        d/bdo
   :big                        d/big
   :blockquote                 d/blockquote
   :body                       d/body
   :br                         d/br
   :button                     d/button
   :canvas                     d/canvas
   :caption                    d/caption
   :circle                     d/circle
   :cite                       d/cite
   :clipPath                   d/clipPath
   :code                       d/code
   :col                        d/col
   :colgroup                   d/colgroup
   :data                       d/data
   :datalist                   d/datalist
   :dd                         d/dd
   :defs                       d/defs
   :del                        d/del
   :details                    d/details
   :dfn                        d/dfn
   :div                        d/div
   :dl                         d/dl
   :dt                         d/dt
   :ellipse                    d/ellipse
   :em                         d/em
   :embed                      d/embed
   :fieldset                   d/fieldset
   :figcaption                 d/figcaption
   :figure                     d/figure
   :footer                     d/footer
   :form                       d/form
   :g                          d/g
   :h1                         d/h1
   :h2                         d/h2
   :h3                         d/h3
   :h4                         d/h4
   :h5                         d/h5
   :h6                         d/h6
   :head                       d/head
   :header                     d/header
   :hr                         d/hr
   :html                       d/html
   :i                          d/i
   :iframe                     d/iframe
   :image                      d/image
   :img                        d/img
   :input                      d/input
   :ins                        d/ins
   :kbd                        d/kbd
   :keygen                     d/keygen
   :label                      d/label
   :legend                     d/legend
   :li                         d/li
   :line                       d/line
   :linearGradient             d/linearGradient
   :link                       d/link
   :main                       d/main
   :mark                       d/mark
   :mask                       d/mask
   :menu                       d/menu
   :menuitem                   d/menuitem
   :meta                       d/meta
   :meter                      d/meter
   :nav                        d/nav
   :noscript                   d/noscript
   :object                     d/object
   :ol                         d/ol
   :optgroup                   d/optgroup
   :option                     d/option
   :output                     d/output
   :p                          d/p
   :param                      d/param
   :path                       d/path
   :pattern                    d/pattern
   :polygon                    d/polygon
   :polyline                   d/polyline
   :pre                        d/pre
   :progress                   d/progress
   :q                          d/q
   :radialGradient             d/radialGradient
   :rect                       d/rect
   :rp                         d/rp
   :rt                         d/rt
   :ruby                       d/ruby
   :s                          d/s
   :samp                       d/samp
   :script                     d/script
   :section                    d/section
   :select                     d/select
   :small                      d/small
   :source                     d/source
   :span                       d/span
   :stop                       d/stop
   :strong                     d/strong
   :style                      d/style
   :sub                        d/sub
   :summary                    d/summary
   :sup                        d/sup
   :svg                        d/svg
   :table                      d/table
   :tbody                      d/tbody
   :td                         d/td
   :text                       d/text
   :textarea                   d/textarea
   :tfoot                      d/tfoot
   :th                         d/th
   :thead                      d/thead
   :time                       d/time
   :title                      d/title
   :tr                         d/tr
   :track                      d/track
   :tspan                      d/tspan
   :u                          d/u
   :ul                         d/ul
   :var                        d/var
   :video                      d/video
   :wbr                        d/wbr})


(defn- make-component [tag attrs content]
  (let [component (or
                    (get tag->component tag)

                    (let [path-segments (conj
                                          (str/split (namespace tag) #"\.")
                                          (name tag))
                          item (reduce
                                 #(aget %1 %2)
                                 js/window
                                 path-segments)]
                      (f/factory item)))
        sub-items (map interpret content)]
    (apply component attrs sub-items)))

(defn element
  "Render an element vector as a HTML element."
  [element]
  (let [[tag attrs content] (normalize/element element)

        classes (set (:class attrs))]
    (make-component tag
                    (-> attrs
                        (dissoc :class)
                        (#(if (empty? classes)
                            %
                            (assoc % :className (str/join " " classes)))))
                    content)))

(defn- interpret-seq [s]
  (map interpret s))

(extend-protocol Interpretable
  PersistentVector
  (interpret [this]
    (if (util/element? this)
      (element this)
      (interpret-seq this)))

  LazySeq
  (interpret [this]
    (interpret-seq this))

  default
  (interpret [this]
    this))
