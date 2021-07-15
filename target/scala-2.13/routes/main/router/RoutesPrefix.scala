// @GENERATOR:play-routes-compiler
// @SOURCE:D:/data/ITteamproject/ITSD-DT2021-Template -our group/conf/routes
// @DATE:Sat Jul 03 19:17:02 BST 2021


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
