package tut

import java.io.PrintWriter

import scala.tools.nsc.Settings
import scala.tools.nsc.interpreter.IMain

import tut.Zed._

trait IMainPlatform {

  protected def newInterpreter(pw: PrintWriter, settings: Settings): IO[Interp] =
    IO(Scalac(new IMain(settings, pw)))

}
