package views.html.helper

package object twitterBootstrap3 {

  implicit val twitterBootstrapField = new FieldConstructor {
    def apply(elts: FieldElements) = twitterBootstrap3FieldConstructor(elts)
  }

}