package net.rrm.ehour.mail

package object service {
  type CallBack = (Mail, Boolean) => Unit
}
