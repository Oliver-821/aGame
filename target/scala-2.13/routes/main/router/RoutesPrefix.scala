// @GENERATOR:play-routes-compiler
// @SOURCE:D:/data/ITteamproject/a card game/conf/routes
// @DATE:Sat Jul 31 23:54:55 BST 2021


package router {
  object RoutesPrefix {
    private var _prefix: String = "/"
    def setPrefix(p: String): Unit = {
      _prefix = p
    }
    def prefix: String = _prefix
    val byNamePrefix: Function0[String] = { () => prefix }
  }
}
