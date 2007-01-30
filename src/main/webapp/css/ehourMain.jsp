<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/css;" %>

body
{
	background: white;
	font: 0.8em/1.5 Geneva, Arial, Helvetica, sans-serif;
	color: black;
	margin-top: 0;
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
	background-color: #d1e5f9;
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
	background: url(<c:url value="/img/grey/corner_left_top.gif" />) top left no-repeat;
	max-height: 258px;
  	width: 230px;
} 

div.HelpFrame h3
{
  background: 	url(<c:url value="/img/grey/corner_right_top.gif" />) top right no-repeat;
  line-height:	11px;
  padding:		0;
  margin: 0;
  font: bold 1.2em/1.5 Geneva, Arial, Helvetica, sans-serif;
  color: #536e87;
  text-align: center;  
}

div.HelpBody
{
	background: url(<c:url value="/img/grey/pixel_grey.gif" />);
  	margin-top: 0em;
  	padding:0;
	height: 200px;
	overflow: auto;
	margin-left: 5px;
	margin-right: 5px;
	color: #536e87;
	font: 0.75em/1.5 Geneva, Arial, Helvetica, sans-serif;
  
}

div.HelpFrameFooter
{
  background: url(<c:url value="/img/grey/corner_left_bottom.gif" />) bottom left no-repeat;
}

div.HelpFrameFooter p
{
  background: url(<c:url value="/img/grey/corner_right_bottom.gif" />) bottom right no-repeat;
  padding:20px;
  display:block;
  margin:-2em 0 0 0;
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