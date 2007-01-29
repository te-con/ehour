<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/css;" %>

body
{
	background: white;
	font: 0.8em/1.5 Geneva, Arial, Helvetica, sans-serif;
	color: black;
}

div.wrap
{
	margin-left: 50px;

}

div.NavCalFrame
{
  background: url(<c:url value="/img/ovw/gradient_blue.gif" />) top left repeat-x;
  max-height: 258px;
  width: 230px;
}

div.NavCalFrameLeftTop
{
    background: url(<c:url value="/img/ovw/left_top.gif" />) top left no-repeat;
}

div.NavCalFrameRightTop
{
  background: url(<c:url value="/img/ovw//right_top.gif" />) top right no-repeat;
}

div.NavCalFrameRightBottom
{
  background: url(<c:url value="/img/ovw//right_bottom.gif" />) bottom right no-repeat;
}

div.NavCalFrameLeftBottom
{
  background: url(<c:url value="/img/ovw//left_bottom.gif" />) bottom left no-repeat;
}

div.NavCalFrame table 
{
	width: 90%;
	margin-left: 5%;
	margin-right: 5%;
	margin-top: -0.5em;
	border: 0;
	padding: 1px;
}

div.NavCalFrame table tr.hoverable
{
	background-color: #f6f9fc;
	border: solid 2px #ff0000;

	}

div.NavCalFrame table tr.hoverable:hover
{
	background-color: #c2daf5;
    cursor: pointer;
    cursor: hand;
	border: solid 2px #ff0000;
}

div.NavCalFrame table th
{
	background-color: #fefeff;
	color: #536e87;
	font: bold 1em/1.5 Geneva, Arial, Helvetica, sans-serif;
}

div.NavCalFrame table td
{
	color: #536e87;
	text-align: center;
	width: 15px;
	margin: 0;
	padding: 0;
	border: 0px;
}

div.NavCalFrame table td.weekendDayCell
{
	background-color: #edf5fe
}

div.NavCalFrame table td.empty
{
	background-color: transparent;
}

div.NavCalFrame table td.filled
{
	font: bold 1em/1.5 Geneva, Arial, Helvetica, sans-serif;
}

div.HelpFrame
{
  background: url(<c:url value="/img/grey/pixel_grey.gif" />);
  max-height: 258px;
  width: 230px;
}

div.HelpFrameLeftTop
{
  background: url(<c:url value="/img/help/left_top.gif" />) top left no-repeat;
}

div.HelpFrameRightTop
{
  background: url(<c:url value="/img/help/right_top.gif" />) top right no-repeat;
  padding-left: 20px;
  padding-right: 20px;
  margin: 0;
  font: bold 1.2em/1.5 Geneva, Arial, Helvetica, sans-serif;
  color: #536e87;
  text-align: center;
}

div.HelpFrameRightBottom
{
  background: url(<c:url value="/img/help/right_bottom.gif" />) bottom right no-repeat;
}

div.HelpFrameLeftBottom
{
  background: url(<c:url value="/img/help/left_bottom.gif" />) bottom left no-repeat;
}

div.HelpFrameBody
{
	height: 200px;
	overflow: auto;
	margin-left: 5px;
	margin-right: 5px;
	color: #536e87;
	font: 0.75em/1.5 Geneva, Arial, Helvetica, sans-serif;
}

div.Header table 
{

}
	
div.Header table th
{
	vertical-align: bottom;
	color: #a1bcd8;
	font: 1.4em/1.7em Geneva, Arial, Helvetica, sans-serif;
}

div.Header table th a:link
{
	color: #a1bcd8;
	font: 1em/1.5 Geneva, Arial, Helvetica, sans-serif;
	text-decoration: none;
}

div.Header table th a:visited
{
	color: #a1bcd8;
	font: 1em/1.5 Geneva, Arial, Helvetica, sans-serif;
	text-decoration: none;
}

div.Header table th a:hover
{
	color: #a1bcd8;
	font: 1em/1.5 Geneva, Arial, Helvetica, sans-serif;
	text-decoration: underline;
}

div.Header table th a:active
{
	color: #a1bcd8;
	font: 1em/1.5 Geneva, Arial, Helvetica, sans-serif;
	text-decoration: none
}