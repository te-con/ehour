<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/css;" %>

body
{
	background: white;
	font: 0.8em/1.5 Geneva, Arial, Helvetica, sans-serif;
	color: white;
}

div.BackFrame
{
  background: url(<c:url value="/img/bg_grey/corner_left_top.gif" />) top left no-repeat;
  width: 520px;
  margin-left: auto;
  margin-right: auto;
} 

div.BackFrame h3
{
  background: 	url(<c:url value="/img/bg_grey/corner_right_top.gif" />) top right no-repeat;
  font-size:	1.3em;
  padding:		0px;
  margin:		0 0 5px 75px;
}

div.BackFrameBody
{
  background: url(<c:url value="/img/bg_grey/pixel_grey.gif" />) top right repeat-y;
  margin:0px;
  margin-top:-2em;
  padding:15px;
}

div.BackFrameFooter
{
  background: url(<c:url value="/img/bg_grey/corner_left_bottom.gif" />) bottom left no-repeat;
}

div.BackFrameFooter p
{
  background: url(<c:url value="/img/bg_grey/corner_right_bottom.gif" />) bottom right no-repeat;
  padding:15px;
  display:block;
  margin:-2em 0 0 0;
  text-align:right;
  color: #99aec2;
  font: 0.7em/2 Geneva, Arial, Helvetica, sans-serif;
}

div.BlueFrame
{
  background: url(<c:url value="/img/bg_blue/left_top.gif" />) top left no-repeat;
  width: 400px;
  margin-left: 60px;
  margin-right: 60px;
} 

div.BlueFrame h3
{
  background: url(<c:url value="/img/bg_blue/right_top.gif" />) top right no-repeat;
  font: bold 1.2em/1 Geneva, Arial, Helvetica, sans-serif;
  padding: 5px 0 10px 0;
  margin: 0 0 0 70px;

}

div.BlueFrameBody
{
  background: url(<c:url value="/img/bg_blue/blue_pix.gif" />) top right repeat-y;
  margin:0;
  min-height: 250px;
  margin-top:-2em;
  padding:15px;
}

div.BlueFrameFooter
{
  background: url(<c:url value="/img/bg_blue/left_bottom.gif" />) bottom left no-repeat;
}

div.BlueFrameFooter p
{
  background: url(<c:url value="/img/bg_blue/right_bottom.gif" />) bottom right no-repeat;
  padding:15px;
  display:block;
  margin:-2em 0 0 0;
  text-align:right;
}

div.BlueFrame table
{
	border-collapse: collapse; 
	border-spacing: 0px;
	margin-left: 30px;
	width: 90%;
	margin-right: 20px;
}

div.BlueFrame td
{
	padding: 0 20px 0 0;
	margin: 0;
	line-height: 1.1;
}

div.BlueFrame input
{
	background-color: white;
	border: 0;
	margin: 0;
	padding: 0;
	font: 0.9em/1.5 Geneva, Arial, Helvetica, sans-serif;
	color: #233e55;
}

#submitButton
{
	background-color: #a3bed9;
	margin: 0;
	padding: 0;
	font: bold 1em Geneva, Arial, Helvetica, sans-serif;
	color: white;
	border: 0;
	border-bottom-width: 1px;
	border-bottom-style: solid;
	border-bottom-color: #cde4fd;
}

#submitButton:hover
{
	background-color: #a3bed9;
	margin: 0;
	padding: 0;
	font: bold 1em Geneva, Arial, Helvetica, sans-serif;
	color: #576d83;
}