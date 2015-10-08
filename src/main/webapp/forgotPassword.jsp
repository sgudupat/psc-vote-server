<html>
<%@page import="com.plumchoice.asap.util.AsapUtil" %>
<%@page import="com.plumchoice.boost.billing.util.PaymentMethodUtil" %>
<%@page import="com.plumchoice.boost.customer.domain.*" %>
<%@page import="com.plumchoice.boost.order.domain.ActiveProduct" %>
<%@page import="com.plumchoice.boost.web.CustomerDisplayAction" %>
<%@page import="com.plumchoice.boost.web.controller.ControllerUtils" %>
<%@page import="java.util.Calendar" %>
<%@ page import="java.util.EnumSet" %>
<%@page import="com.plumchoice.boost.SpringApplicationContext"%>
<%@page import="com.plumchoice.boost.user.domain.User"%>
<%@page import="com.plumchoice.boost.user.facade.UserFacade"%>
<%@page import="com.plumchoice.boost.web.LoginUtils"%>
<%@page import="com.plumchoice.boost.web.config.domain.UIConfig"%>
<%@page import="com.plumchoice.boost.web.config.facade.ConfigFacade"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%
	User user = null;
	String keyValue = pageContext.getAttribute("key");
		try {
            UserFacade userFacade = (UserFacade) SpringApplicationContext.getBean("userFacade");
			isSessionLoggedIn = !userFacade.isUserNull(user);
		} catch (Exception e) {
			isSessionLoggedIn = false;
			LoggerUtils.error(JSP_NAME, e.getMessage());
		}
	}

	if (!isSessionLoggedIn) {
		String returnLoginPage = LOGIN_JSP_NAME;
		LoggerUtils.debug(JSP_NAME, "Session User NOT present!", JSP_LOGS_ENABLED);
		 if (isAjaxCall) {
           LoggerUtils.debug(JSP_NAME, "Ajax called!", JSP_LOGS_ENABLED);
            out.write("LOGIN");
            return;
        }
		response.encodeRedirectURL(returnLoginPage);
		response.sendRedirect(returnLoginPage);
		return;

	}

	LoggerUtils.debug(JSP_NAME, "Session User is present.", JSP_LOGS_ENABLED);

	String reqURI = request.getRequestURI();
	LoggerUtils.debug(JSP_NAME, "reqURI: " + reqURI, JSP_LOGS_ENABLED);

	String fileName = reqURI.substring(reqURI.lastIndexOf("/") + 1);
	LoggerUtils.debug(JSP_NAME, "fileName: " + fileName, JSP_LOGS_ENABLED);

	boolean isAllowed = false;
	List<UIConfig> menuConfig = null;

	try {
		Object objIsProcessPage = pageContext.getAttribute("isProcessPage", pc);
		Object objIsDisplayablePage = pageContext.getAttribute("isDisplayablePage", pc);

		boolean isProcessPage = (objIsProcessPage == null) ? false : (Boolean) objIsProcessPage;
		boolean isDisplayablePage = (objIsDisplayablePage == null) ? false : (Boolean) objIsDisplayablePage;

		if (isProcessPage) {
			isAllowed = true;
			LoggerUtils.debug(JSP_NAME, "In a valid process page: " + fileName
					+ " , will skip access control.", JSP_LOGS_ENABLED);
		}
		if (isDisplayablePage) {
			isAllowed = true;
			LoggerUtils.debug(JSP_NAME, "In a valid Displayable page: " + fileName
					+ " , will skip access control.", JSP_LOGS_ENABLED);
		}
	} catch (Exception e) {
		LoggerUtils.error(JSP_NAME, e.getMessage());
		// and leave it to false for now...
	}

	if (!isAllowed) {
		LoggerUtils.debug(JSP_NAME, "In a display page, will perform access control.", JSP_LOGS_ENABLED);

		String agentRole = user.getEntryAgent().getAgentRole().getRoleType();
		menuConfig = (List<UIConfig>) session.getAttribute("menuConfig");
		if (menuConfig == null) {
			try {
				ConfigFacade configFacade = (ConfigFacade) SpringApplicationContext.getBean("configFacadeBean");
				menuConfig = configFacade.getMenuConfig(agentRole);

				session.setAttribute("menuConfig", menuConfig);

			} catch (Exception e) {
				LoggerUtils.error(JSP_NAME, e.getMessage());
			}
		} // else nothing - is already here inside the session

		isAllowed = LoginUtils.isAllowed(menuConfig, fileName, agentRole);
		LoggerUtils.debug(JSP_NAME, "isAllowed: " + isAllowed, JSP_LOGS_ENABLED);
	}

	if (!isAllowed) {
		LoggerUtils.debug(JSP_NAME, "NOT Allowed! Will redirect ...", JSP_LOGS_ENABLED);
        if (isAjaxCall) {
            LoggerUtils.debug(JSP_NAME, "Ajax called!", JSP_LOGS_ENABLED);
            out.write("LOGOUT");
            return;
        }
		response.sendRedirect(LOGOUT_ACTION);
		return;
	}
	LoggerUtils.debug(JSP_NAME, "Allowed!", JSP_LOGS_ENABLED);


	LoggerUtils.debug(JSP_NAME, "End", JSP_LOGS_ENABLED);
%>
<% pageContext.setAttribute("_pageTitle", "Customer Account Details", PageContext.REQUEST_SCOPE); %>

<jsp:include page="xxBOOSTHeader.jsp" flush="true"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<link type="text/css" rel="stylesheet" href="yui/calendar/assets/skins/sam/calendar.css">
<script type="text/javascript" src="js/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="js/jquery.dataTables.min.js"></script>

<script type="text/javascript" src="yui/yahoo-dom-event/yahoo-dom-event.js"></script>
<script type="text/javascript" src="yui/animation/animation-min.js"></script>
<script type="text/javascript" src="yui/container/container-min.js"></script>
<script type="text/javascript" src="js/dialog_popup_util.js"></script>

<script type="text/javascript" src="yui/calendar/calendar-min.js"></script>
<script type="text/javascript" src="yui/json/json-min.js"></script>

<script type="text/javascript" src="js/xxboostCalendar.js"></script>

<script type="text/javascript" SRC="xxasap_record.js"></script>
<script type="text/javascript" src="js/xxboost_customer_care.js"></script>
<script type="text/javascript" src="js/customerCareProductTasks.js"></script>
<script type="text/javascript" src="js/adjustInvoice.js"></script>
<script type="text/javascript" src="js/customerCarePaymentsTab.js"></script>
<script type="text/javascript" src="js/changeProductCalculations.js"></script>
<script type="text/javascript" src="js/oneTimeInvoice.js"></script>
<script type="text/javascript" src="xxboost_resubmit_unpaid_invoice.js"></script>

<script type="text/javascript" src="js/xxboost_pricing.js"></script>
<script type="text/javascript" src="js/xxboost_pricing_util.js"></script>
<script type="text/javascript" src="js/xxboost_task_update_pricing.js"></script>
<script type="text/javascript" src="js/modifyProduct.js"></script>
<script type="text/javascript" src="js/timeDisplay.js"></script>
<script type="text/javascript" src="js/customer_inventory_tasks.js"></script>

<link href="css/resubmit_unpaid_invoices.css" rel="stylesheet" type="text/css"/>
<link href="css/jquery.dataTables-edited.css" rel="stylesheet" type="text/css"/>
<link type="text/css" rel="stylesheet" href="yui/assets/skins/sam/container.css">

<body class="yui-skin-sam">

<%
    System.out.println("CUSTOMER PAGE EXECUTE");
    CustomerDisplayAction action = new CustomerDisplayAction();
    action.execute(request, response, pageContext);
    System.out.println("CUSTOMER PAGE EXECUTE ===== 1");
%>
<% response.setHeader("Cache-Control", "no-cache");
    String userName = user.getEntryAgent().getUserName();
%>
<div id="pageLoadingCover"></div>
<div id="pageLoadingCircle"><img src="/OA_MEDIA/xxpluimg/long_loading_bar.gif"/></div>
<input type="hidden" id="saleAgent1" name="saleAgent1" value="<%=userName%>"/>
<%

    String subMenu = StringUtils.defaultString(request.getParameter("subMenu"));
    String dispbutton = StringUtils.defaultString(request.getParameter("dispbutton"));
    String sbsproduct = StringUtils.defaultString(request.getParameter("sbsproduct"));
    String customerno = StringUtils.defaultString(request.getParameter("customerno"));
    String emailr = (StringUtils.defaultString(request.getParameter("EmailaddR")));
    String phoner = StringUtils.defaultString(request.getParameter("PhoneR"));
    String fnamer = (StringUtils.defaultString(request.getParameter("FnameR")));
    String cityr = StringUtils.defaultString(request.getParameter("CityR"));
    String companyr = (StringUtils.defaultString(request.getParameter("CompanyR")));
    String lnamer = (StringUtils.defaultString(request.getParameter("LnameR")));
    String fannumber = StringUtils.defaultString(request.getParameter("FanNumber"));
    String countryr = StringUtils.defaultString(request.getParameter("CountryR"));
    String ubaccountnumber = StringUtils.defaultString(request.getParameter("ubaccountnumber"));
    String banNumber = StringUtils.defaultString(request.getParameter("banNumber"));

    pageContext.setAttribute("dispbutton", dispbutton, PageContext.REQUEST_SCOPE);
    pageContext.setAttribute("email", emailr, PageContext.REQUEST_SCOPE);
    pageContext.setAttribute("phone", phoner, PageContext.REQUEST_SCOPE);
    pageContext.setAttribute("fname", fnamer, PageContext.REQUEST_SCOPE);
    pageContext.setAttribute("lname", lnamer, PageContext.REQUEST_SCOPE);
    pageContext.setAttribute("city", cityr, PageContext.REQUEST_SCOPE);
    pageContext.setAttribute("company", companyr, PageContext.REQUEST_SCOPE);
    pageContext.setAttribute("country", countryr, PageContext.REQUEST_SCOPE);
// Back To Result end here

    String cctypeselect = StringUtils.defaultString(request.getParameter("ccTypeSelect"));
    String ccnumber = StringUtils.defaultString(request.getParameter("ccNumber"));
    String ccsecuritycode = StringUtils.defaultString(request.getParameter("ccSecurityCode"));
    String ccExpMonth = StringUtils.defaultString(request.getParameter("ccMonth"));
    String ccExpYear = StringUtils.defaultString(request.getParameter("ccYear"));

    /*---------------------CR 84--------------------*/

    String invoice = StringUtils.defaultString(request.getParameter("invoice"));
    String btn = StringUtils.defaultString(request.getParameter("btn"));
    String cc = StringUtils.defaultString(request.getParameter("cc"));
    String ccMerchant = StringUtils.defaultString(request.getParameter("ccMerchant"));
    String order = StringUtils.defaultString(request.getParameter("order"));
    String ticket = StringUtils.defaultString(request.getParameter("ticket"));
    String fannum = StringUtils.defaultString(request.getParameter("fannum"));
    String domain = StringUtils.defaultString(request.getParameter("domain"));
    String docviewid = StringUtils.defaultString(request.getParameter("docviewid"));
    String inventoryid = StringUtils.defaultString(request.getParameter("inventoryid"));
    String bidOrderNumber = StringUtils.defaultString(request.getParameter("bidOrderNumber"));
    String searchBy = StringUtils.defaultString(request.getParameter("searchBy"));
    //added  -CR 441
    String ubaccno = StringUtils.defaultString(request.getParameter("ubaccno"));

    /*---------------------CR 84--------------------*/

    String custAccountId = StringUtils.defaultString(request.getParameter("custid"));
    // Added code for Authorization user
    String emailauth = StringUtils.defaultString(request.getParameter("EmailAuth"), "*Email Address");
    String fnameauth = StringUtils.defaultString(request.getParameter("FnameAuth"), "*First Name");
    String phoneauth = StringUtils.defaultString(request.getParameter("PhoneAuth"), "*Primary Phone");
    String lnameauth = StringUtils.defaultString(request.getParameter("LnameAuth"), "*Last Name");
    String acntStatus = "Active";
    String ccSaccAddFlag = "";
    String ccDisable = "";

    java.util.ArrayList accountReasonCodes = (java.util.ArrayList) pageContext.getAttribute("ACCOUNT_REASON_CODES", PageContext.REQUEST_SCOPE);
    java.util.ArrayList customerStatusCodes = (java.util.ArrayList) pageContext.getAttribute("CUSTOMER_STATUS_LOOKUP", PageContext.REQUEST_SCOPE);
    java.util.ArrayList countryLookup = (java.util.ArrayList) pageContext.getAttribute("COUNTRY_LOOKUP", PageContext.REQUEST_SCOPE);
    java.util.ArrayList pmntLookup = (java.util.ArrayList) pageContext.getAttribute("PAYMENT_TYPE_LOOKUP", PageContext.REQUEST_SCOPE);
    java.util.ArrayList roleTypeLookup = (java.util.ArrayList) pageContext.getAttribute("ROLE_TYPE_LOOKUP", PageContext.REQUEST_SCOPE);
    java.util.ArrayList productReasonCodes = (java.util.ArrayList) pageContext.getAttribute("PRODUCT_REASON_CODES", PageContext.REQUEST_SCOPE);
    AgentRole userRole = user.getEntryAgent().getAgentRole();
    pageContext.setAttribute("userRole", userRole.getRoleType(), PageContext.REQUEST_SCOPE);
    pageContext.setAttribute("custAccountId", custAccountId, PageContext.REQUEST_SCOPE);
%>
<script type="text/javascript">
    initDialog("pageLoadingCircle", 220);
    openDialog("pageLoadingCircle");
</script>
<div id="top">
    <div class="left">&nbsp;</div>
    <div class="center">&nbsp;</div>
    <div class="right">&nbsp;</div>
</div>
<!-- Main wrapper Table start here -->

<table width="985" border="0" align="center" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
<tr>
<td height="500" align="left" valign="top" class="padLR30">
<%
    String errorMessage = StringUtils.defaultString(request.getParameter("errList"));
    if (StringUtils.isBlank(errorMessage)) {
        errorMessage = StringUtils.defaultString((String) pageContext.getAttribute("errList", PageContext.REQUEST_SCOPE));
    }
    if (StringUtils.isBlank(errorMessage)) {
        errorMessage = StringUtils.defaultString((String) request.getSession().getAttribute("errList"));
        request.getSession().setAttribute("errList", null);
    }
    if (StringUtils.isNotBlank(errorMessage)) {
%>
<div id="errorMessage" class="saleserror"><img src="/OA_MEDIA/xxpluimg/error_icon.gif" alt=""/><%=errorMessage%>
</div>
<%
    }
    String successMessage = StringUtils.defaultString(request.getParameter("sucList"));
    if (StringUtils.isBlank(successMessage)) {
        successMessage = StringUtils.defaultString((String) pageContext.getAttribute("sucList", PageContext.REQUEST_SCOPE));
    }
    if (StringUtils.isBlank(successMessage)) {
        successMessage = StringUtils.defaultString((String) request.getSession().getAttribute("sucList"));
        request.getSession().setAttribute("sucList", null);
    }

    if (StringUtils.isNotBlank(successMessage)) {
%>
<div id="successMessage" class="confirmmessage">
    <img src="/OA_MEDIA/xxpluimg/success_icon.gif"
         alt=""/> <%=successMessage%>
</div>
<%
    }
%>

<div id="errorMsgFromJs"></div>
<div id="successMsgFromJs"></div>
<div class="content">
<%

    CustomerDomain customerDomain = (CustomerDomain) pageContext.getAttribute("CUSTOMER_INFO", PageContext.REQUEST_SCOPE);
    Customer customer = customerDomain.getCustomer();
    CreditCardInfo ccInfo = new CreditCardInfo();
    if (customerDomain.getPaymentMethod().getCreditCardInfo() != null) {
        ccInfo = customerDomain.getPaymentMethod().getCreditCardInfo();
    }

    BTNInfo btnInfo = new BTNInfo();
    if (customerDomain.getPaymentMethod().getBtnInfo() != null) {
        btnInfo = customerDomain.getPaymentMethod().getBtnInfo();
    }

    BANInfo banInfo = new BANInfo();
    if (customerDomain.getPaymentMethod().getBanInfo() != null) {
        banInfo = customerDomain.getPaymentMethod().getBanInfo();
    }

    String checkOpt = customerDomain.getCustomer().isOptsIn() ? "Checked" : "";
    String ccStreet = "";
    String ccApartName = "";
    String ccCityName = "";
    String ccStateName = "";
    String ccCuntryName = "";
    String ccZipCode = "";
    String ccFullCustName = "";
    String ccCheck = "checked";
    String ccClass = "nodisplay";
    String billingContactId = "";

    if (customerDomain.getBillingContact() != null && customerDomain.getBillingContact().getContactType() == BillingContactType.CC_ADDR) {
        billingContactId = String.valueOf(customerDomain.getBillingContact().getBillingContactID());
        ccStreet = StringUtils.defaultString(customerDomain.getBillingContact().getAddress1());
        ccApartName = StringUtils.defaultString(customerDomain.getBillingContact().getAddress2());
        ccCityName = StringUtils.defaultString(customerDomain.getBillingContact().getCity());
        ccStateName = StringUtils.defaultString(customerDomain.getBillingContact().getState());
        ccCuntryName = StringUtils.defaultString(customerDomain.getBillingContact().getCountry());
        ccZipCode = StringUtils.defaultString(customerDomain.getBillingContact().getZip());
        ccFullCustName = StringUtils.defaultString(customerDomain.getBillingContact().getCustomerName());
        ccCheck = "unchecked";
        ccClass = "";
    }
%>
<form name="custDetailForm" method="post" ACTION="">
<input name="ccBillingContactExistsHid" type="hidden" id="ccBillingContactExistsHid" value="<%=billingContactId%>"/>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td width="88%"><h1 style="WORD-BREAK:BREAK-ALL">Customer Account Details
            - <%=StringUtils.defaultString(customer.getFirstName())%> <%=StringUtils.defaultString(customer.getLastName())%>
        </h1></td>
<!-- added ubaccno - CR441 -->
        <td width="12%"><a
                href="processfindcustomer.form?menu=topmenu&subMenu=customerCare&sbsproduct=<%=sbsproduct%>&customerno=<%=customerno%>&email=<%=ControllerUtils.encodeURL(emailr)%>&phone=<%=phoner%>&fname=<%=ControllerUtils.encodeURL(fnamer)%>&lname=<%=ControllerUtils.encodeURL(lnamer)%>&fannumber=<%=fannumber%>&city=<%=cityr%>&company=<%=ControllerUtils.encodeURL(companyr)%>&country=<%=countryr%>&ubaccountnumber=<%=ubaccountnumber%>&banNumber=<%=banNumber%>&invoice=<%=invoice%>&ubaccno=<%=ubaccno%>&btn=<%=btn%>&cc=<%=cc%>&ccMerchant=<%=ccMerchant%>&order=<%=order%>&ticket=<%=ticket%>&domain=<%=ControllerUtils.encodeURL(domain)%>&fannum=<%=fannum%>&docviewid=<%=docviewid%>&inventoryid=<%=inventoryid%>&bidOrderNumber=<%=bidOrderNumber%>&searchBy=<%=searchBy%>&fromBackButton=Y">
            <% if ("Y".equals(dispbutton)) { %>
            <img id="BacktoResultsBtnCustDetails" src="/OA_MEDIA/xxpluimg/back_result_button.gif"
                 onMouseOver="changeImgMouseOver(this,this.src);"
                 onMouseOut="changeImgMouseOut(this,this.src);" border=0/></a></td>
        <% } %>
        <td width="12%">
            <a href="javascript:showSaveChanges();"> <input type="image" style="margin-left: 5px" name="save1" id="save1" style="MARGIN-LEFT: 2px"
                               src="/OA_MEDIA/xxpluimg/save_changes_button.gif"
                               onclick="javascript:showSaveChanges();"
                               onMouseOver="changeImgMouseOver(this,this.src);"
                               onMouseOut="changeImgMouseOut(this,this.src);"/>
            </a></td>
    </tr>
</table>
<div class="custaccountdetails">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
<tr>
<td colspan="2" rowspan="5" valign="top">
    <table width="100%" border="0" cellpadding="0" cellspacing="0">
        <%
            if (AgentRole.SMBSalesAgent != userRole
                    && AgentRole.SMBEntryAgent != userRole
                    && AgentRole.SMBAgentCI != userRole) {

                if (customer.getCustomerStatus() != null) {
                    acntStatus = StringUtils.capitalize(StringUtils.lowerCase(customer.getCustomerStatus().toString()));
                }
        %>
        <tr>
            <td width="37%"><b>Account Status </b></td>
            <td width="63%"><a id="customerCare.customerDetails.accountStatus"
                               href="javascript:showStatus();"><%=acntStatus%>
            </a></td>
        </tr>
        <%
            }
        %>
        <tr>
            <td width="37%">Customer Number</td>
            <td width="63%"><%=custAccountId%>
            </td>
        </tr>
        <tr>
            <td width="37%">*Email Address</td>
            <td width="63%">
                <input name="Emailaddress" type="text" id="Emailaddress"
                       value="<%=StringUtils.defaultString(customer.getEmail())%>" size="30" TABINDEX=2
                       onblur="loadvalue(this);" onkeypress="checkletter(this);" maxlength="240"/></td>
        </tr>
        <tr>
            <td>*Office Phone</td>
            <td height="29">
                <input name="OfficePhone" type="text" id="OfficePhone"
                       value="<%=StringUtils.defaultString(customer.getOfficePhone())%>" size="30" maxlength="250"
                       TABINDEX=3
                       onblur="loadvalue(this);" onkeypress="checkletter(this);"/></td>
        </tr>
        <tr>
            <td>Home Phone</td>
            <td><input name="HomePhone" type="text" id="HomePhone"
                       value="<%=StringUtils.defaultString(customer.getHomePhone())%>" size="30" maxlength="250"
                       TABINDEX=4
                       onblur="loadvalue(this);" onkeypress="checkletter(this);"/>
            </td>
        </tr>
        <tr>
            <td>Mobile Phone</td>
            <td><input name="MobPhone" type="text" id="MobPhone"
                       value="<%=StringUtils.defaultString(customer.getMobilePhone())%>" size="30" maxlength="250"
                       TABINDEX=5
                       onblur="loadvalue(this);" onkeypress="checkletter(this);"/>
            </td>
        </tr>
        <tr>
            <td colspan="2"><input name="Optcheckbox" type="checkbox" id="Optcheckbox" <%=checkOpt%> TABINDEX=6
                                   onblur="loadvalue(this);" onkeypress="checkletter(this);"/>
                Opt to receive AT&amp;T special offers.
            </td>
        </tr>
        <tr>
            <td colspan="2">&nbsp;</td>
        </tr>
        <tr>
            <td colspan="2">
                <input type="image" id="ResetPasswordBtn"
                       src="/OA_MEDIA/xxpluimg/reset_password_button.gif"
                       onclick="javascript:showresetPassword();"
                       onMouseOver="changeImgMouseOver(this,this.src);"
                       onMouseOut="changeImgMouseOut(this,this.src);"/></td>
        </tr>
        <% String agentrole;
            agentrole = userRole.getRoleType().toLowerCase();
            if (agentrole.indexOf("supervisor") >= 0 || agentrole.indexOf("billing") >= 0 || agentrole.indexOf("admin") >= 0) {
        %>
        <tr>
            <td colspan="2">
                <input id="ResubmitUnPaidInvoicesBtn" type='button' value='Resubmit Unpaid Invoices'
                       onfocus="this.blur();"
                       onclick='javascript:showUnpaidInvoicesPopup("<%=custAccountId%>", "<%=userName%>"); return false;'/>
            </td>
        </tr>
        <%} %>
        <tr>
            <td colspan="2">
                <input id="NotesBtn" type='button' value='Notes' onclick='javascript:enableNotesPopup();'/>
            </td>
        </tr>
        <!-- End CR159 resubmit unpaid invoices -->
    </table>
</td>
<td width="7%">&nbsp;</td>
<td width="51%" colspan="2" rowspan="5" valign="top">
    <table width="100%" border="0" cellpadding="0" cellspacing="0">
        <tr>
            <td width="11%" nowrap="nowrap">*First Name</td>
            <td width="40%">
                <input name="Fname" type="text" id="Fname"
                       value="<%=StringUtils.defaultString(customer.getFirstName())%>"
                       size="30" TABINDEX=8 onblur="loadvalue(this);" onkeypress="checkletter(this);"
                       maxlength="150"/></td>
        </tr>
        <tr>
            <td nowrap="nowrap">*Last Name</td>
            <td><input name="Lname" type="text" id="Lname"
                       value="<%=StringUtils.defaultString(customer.getLastName())%>"
                       size="30" TABINDEX=9 onblur="loadvalue(this);" onkeypress="checkletter(this);"
                       maxlength="150"/>
            </td>
        </tr>
        <tr>
            <td>*Company</td>
            <td><input name="Company" type="text" id="Company"
                       value="<%=StringUtils.defaultString(customer.getCompany())%>"
                       size="30" maxlength="240" TABINDEX=11 onblur="loadvalue(this);" onkeypress="checkletter(this);"/>
            </td>
        </tr>
        <tr>
            <td>*Address1</td>
            <td><input name="Address1" type="text" id="Address1"
                       value="<%=StringUtils.defaultString(customer.getAddress1())%>" size="30"
                       maxlength="240" TABINDEX=12 onblur="loadvalue(this);" onkeypress="checkletter(this);"/></td>
        </tr>
        <tr>
            <td>Address2</td>
            <td><input name="Address2" type="text" id="Address2"
                       value="<%=StringUtils.defaultString(customer.getAddress2())%>" size="30"
                       maxlength="240" TABINDEX=13 onblur="loadvalue(this);" onkeypress="checkletter(this);"/></td>
        </tr>
        <%
            // Embargoed CountryList start here
            String blackListFlag = "N";
            if (countryLookup.size() > 0) {
                blackListFlag = "Y";
                for (int j = 0; j < countryLookup.size(); j++) {
                    UIConfig countryCode1 = (UIConfig) countryLookup.get(j);
                    if (StringUtils.defaultString(customer.getCountry()).equals(countryCode1.getParent())) {
                        blackListFlag = "N";
                    }
                }
            }
        %>
        <tr>
            <td height="31" nowrap="nowrap">*Country</td>
            <td>
                <%
                    if ("Y".equals(blackListFlag) || PaymentType.UB == customer.getPrimaryPaymentMethod()) {
                %>
                <input name="country" type="text" id="country"
                       value="<%=StringUtils.defaultString(customer.getCountry())%>" size="12"
                       onkeypress="checkletter(this);" style="width:186px" TABINDEX=14/>
                <%
                } else {
                %>
                <select name="country" id="country" TABINDEX=15 onchange="checkNonUSCountry();"
                        onblur="loadvalue(this);" onkeypress="checkletter(this); " style="width:186px">
                    <option value="">Select</option>
                    <%
                        for (int j = 0; j < countryLookup.size(); j++) {
                            UIConfig countryCode1 = (UIConfig) countryLookup.get(j);
                            if (StringUtils.defaultString(customer.getCountry()).equals(countryCode1.getParent())) {
                    %>
                    <option value="<%=countryCode1.getParent()%>" selected><%=countryCode1.getConfig()%>
                    </option>
                    <%
                    } else {
                    %>
                    <option value="<%=countryCode1.getParent()%>"><%=countryCode1.getConfig()%>
                    </option>
                    <%
                            }
                        }
                    %>
                </select>
                <%
                    }
                    pageContext.setAttribute("zipCode", StringUtils.defaultString(customer.getZip()), PageContext.REQUEST_SCOPE);
                %>
            </td>
        </tr>
        <tr>
            <td nowrap="nowrap">*Zip/Postal Code</td>
            <td><input name="zip" type="text" id="zip"
                       value="<%=StringUtils.defaultString(customer.getZip())%>" size="15"
                       onblur="getCustCareCityState();loadvalue(this)" TABINDEX=16 onkeypress="checkletter(this);"
                       maxlength="60"/></td>
        </tr>
        <tr>
            <td nowrap="nowrap">*State/Province</td>
            <td><input name="state" type="text" id="state"
                       value="<%=StringUtils.defaultString(customer.getState())%>" size="20" TABINDEX=17
                       onkeypress="checkletter(this);" maxlength="60"></td>
        </tr>
        <tr>
            <td>*City</td>
            <td><input name="city" type="text" id="city"
                       value="<%=StringUtils.defaultString(customer.getCity())%>" size="20"
                       onblur="loadvalue(this);" TABINDEX=18 onkeypress="checkletter(this);" maxlength="100"/></td>
        </tr>
        <td><input name="custid" type="hidden" id="custid" value="<%=custAccountId%>"/></td>
        <td><input name="Emailhid" type="hidden" id="Emailhid"
                   value="<%=StringUtils.defaultString(customer.getEmail())%>"/></td>
        <td><input name="invoiceEmail" type="hidden" id="invoiceEmail"
                   value="<%=StringUtils.defaultString(customer.getInvoiceEmail())%>"/></td>
        <td><input name="sendFutureInvoice" type="hidden" id="sendFutureInvoice"
                   value="<%=StringUtils.defaultString(customer.getSendFutureInvoices())%>"/></td>
        </tr>
    </table>
</td>
</tr>
<tr>
    <td>&nbsp;</td>
</tr>
<tr>
    <td>&nbsp;</td>
</tr>
<tr>
    <td>&nbsp;</td>
</tr>
<tr>
    <td>&nbsp;</td>
</tr>
</table>
</div>

<%
    String tab1Class = StringUtils.defaultString(((String) pageContext.getAttribute("billingTab", PageContext.REQUEST_SCOPE)));
    String tab2Class = StringUtils.defaultString(((String) pageContext.getAttribute("activeProdTab", PageContext.REQUEST_SCOPE)));
    String tab3Class = "";
    String tab4Class = StringUtils.defaultString(((String) pageContext.getAttribute("authUserTab", PageContext.REQUEST_SCOPE)));
    String tab5Class = "";
    String tab6Class = "";
    String tab7Class = "";
    String billClass = "nodisplay";
    String authUserClass = "nodisplay";
    String activeProdClass = "nodisplay";

    if (!("").equals(tab1Class)) {
        billClass = "cusdetbilling";
    }
    if (!("").equals(tab2Class)) {
        activeProdClass = "cusdetActiveProd";
    }
    if (!("").equals(tab4Class)) {
        authUserClass = "cusdetAuthusers";
    }

    if (request.getParameter("checkselect") != null && ("Y").equals(request.getParameter("checkselect"))) {
        tab6Class = "Current";
    }
    if (("").equals(tab1Class) && ("").equals(tab2Class) && ("").equals(tab3Class) && ("").equals(tab4Class) && ("").equals(tab5Class) && ("").equals(tab6Class) && ("").equals(tab7Class) && (request.getParameter("checkselect") == null || ("").equals(request.getParameter("checkselect")))) {
        tab1Class = "Current";
        tab4Class = "";
        tab5Class = "";
        tab6Class = "";
        tab7Class = "";
        billClass = "cusdetbilling";
        authUserClass = "nodisplay";
    }
%>
<!-- Mervin Removed code for show tab -->
<div id="minimenu">
    <div id="minitabs">
        <ul>
            <li id="li1" class="<%=tab1Class%>"><a id='PaymentMethod' href="javascript:showTab('minitabscontent1')">
                Payment Method</a></li>
            <li id="li2" class="<%=tab2Class%>"><a id='Products' href="javascript:showTab('minitabscontent2')">
                Products</a></li>
            <li id="li7" class="<%=tab7Class%>"><a id='PendingOrder' href="javascript:showTab('minitabscontent7')">
                Pending Order</a></li>
            <li id="li3" class="<%=tab3Class%>"><a id='History' href="javascript:showTab('minitabscontent3')">
                History</a></li>
            <li id="li4" class="<%=tab4Class%>"><a id='Users' href="javascript:showTab('minitabscontent4')"> Users</a>
            </li>
            <li id="li5" class="<%=tab5Class%>"><a id='Payments' href="javascript:showTab('minitabscontent5')">
                Payments</a></li>
            <%
                if (userRole.equals(AgentRole.BillingWHAdministrator)
                        || userRole.equals(AgentRole.BillingAdministrator)
                        || userRole.equals(AgentRole.SMBAdministrator)) {
            %>
            <li id="li6" class="<%=tab6Class%>"><a id='Tasks' href="javascript:showTab('minitabscontent6')"> Tasks</a>
            </li>
            <%
            } else {
            %>
            <li id="li6" class="<%=tab6Class%>">
                    <%
            }
            %>
        </ul>
    </div>
</div>
<%
    if (StringUtils.defaultString(request.getParameter("minitabscontent1")).equals("tab2")) {
%>
<script>showTab("minitabscontent2");</script>
<%
    }
    if (StringUtils.defaultString(request.getParameter("minitabscontent1")).equals("tab4")) {
%>
<script>showTab("minitabscontent4");</script>
<%
    }
%>
<!--Start : Payment Method popup-->
<div id='minitabscontent1' class="<%=billClass%>">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
<tr>
        <%//Default MOP and also BTN information display%>
<table width="100%" border="0" style="margin-left:8px;margin-bottom:10px">
    <tr>
        <td class="bold"> Default Payment Method Type
            <%
                String oldBtnNumber = StringUtils.defaultString(btnInfo.getPhoneNumber());
                String oldSecurityCode = StringUtils.defaultString(btnInfo.getSecurityCode());
                String oldBanNumber = StringUtils.defaultString(banInfo.getBillingAccountNumber());
                boolean pickBTN = PaymentMethodUtil.isBTNAllowed(customer.getState());
                String disabledBTN = pickBTN ? "" : "disabled";
                boolean pickBAN = PaymentMethodUtil.isBANAllowed(customer.getState());
                String disabledBAN = pickBAN ? "" : "disabled";
                PaymentType primaryPaymentMethod = customer.getPrimaryPaymentMethod();
                String customerPaymentInfo = "";
                if (primaryPaymentMethod != null) {
                    customerPaymentInfo = String.valueOf(primaryPaymentMethod.getTypeChar());
                }
                boolean pickCC = PaymentMethodUtil.isCCAllowed(primaryPaymentMethod, customer.getUbInvoiceAccountNumber(), customer.getCountry());
                boolean pickUB = PaymentMethodUtil.isUBAllowed(customer.getCountry());
            %>
            <input type="hidden" id="customerCare.nonBTNError" value=""/>
            <input type="hidden" id="paymentTypeHidden" name="paymentTypeHidden" value="<%=customerPaymentInfo%>"/>
            <select name="pmntType" id="pmntType" onchange="validateAddressWithPaymentInfo();">
                <option value="" selected>Select</option>
                <%
                    for (int t = 0; t < pmntLookup.size(); t++) {
                        UIConfig pmntCode = (UIConfig) pmntLookup.get(t);
                        if (StringUtils.equals(customerPaymentInfo, pmntCode.getParent())) {
                %>
                <option value="<%=pmntCode.getParent()%>" selected><%=pmntCode.getConfig()%>
                </option>
                <%
                } else if ((pickBTN && StringUtils.equals("B", pmntCode.getParent()))
                        || (pickCC && StringUtils.equals("C", pmntCode.getParent()))
                        || (pickUB && StringUtils.equals("U", pmntCode.getParent()))
                        || (pickBAN && StringUtils.equals("V", pmntCode.getParent()))) {
                %>
                <option value="<%=pmntCode.getParent()%>"><%=pmntCode.getConfig()%>
                </option>
                <%
                        }
                    }
                %>
            </select>
        </td>
    </tr>
    <tr>
        <td class="bold">&nbsp;</td>
    </tr>
    <tr>
        <td class="bold"> Wireline BTN</td>
    </tr>
    <tr>
        <td><input name="btnPhoneNumber" type="text" id="btnPhoneNumber"
                   value="<%=StringUtils.defaultString(btnInfo.getPhoneNumber())%>" size="18" <%= disabledBTN %> />
            <input name="btnPhoneNumberhid" type="hidden" id="btnPhoneNumberhid" value="<%=oldBtnNumber%>"/>
            -
            <input name="btnSecurityCode" type="text" id="btnSecurityCode"
                   value="<%=StringUtils.defaultString(btnInfo.getSecurityCode())%>" size="8" <%= disabledBTN %>/>
            <input name="btnSecurityCodehid" type="hidden" id="btnSecurityCodehid" value="<%=oldSecurityCode%>"/>

            <input type="checkbox" name="taxExemptBox" id="taxExemptBox" disabled="disabled"
                    <% if (btnInfo.isTaxExempt()) { %>
                   checked="checked"
                    <% } %>
                    /> Tax Exempt
        </td>
    </tr>
    <tr>
        <td><input type="checkbox" name="btnCheckbox" id="btnCheckbox"/>
            Please Verify that you have customer authorization to place charge on their AT&amp;T Telco Bill
        </td>
    </tr>
</table>
    <%//CC information display%>
<table width="100%" border="0" style="margin-left:8px">
<tr>
    <td width="37%" class="bold">CC</td>
    <td width="10%">&nbsp;</td>
    <td width="53%">&nbsp;</td>
</tr>
<%
    String newCcAccNumber = "";
    String maskCCNo = "";
    String securityCode = "";
    String maskCCNoHidden = "";
    String securityCodeHidden = "";
    String CCType = "";
    String expDt = "";
    String ccMntStr = "";
    String ccYrStr = "";
    try {
        if (customerDomain.getPaymentMethod().getCreditCardInfo() != null && !"".equals(customerDomain.getPaymentMethod().getCreditCardInfo())) {
            maskCCNo = customerDomain.getPaymentMethod().getCreditCardInfo().getMaskedCardNumber();
            maskCCNoHidden = maskCCNo;
            newCcAccNumber = StringUtils.defaultString(customerDomain.getPaymentMethod().getCreditCardInfo().getPaymentAccountNumber());
            securityCode = customerDomain.getPaymentMethod().getCreditCardInfo().getSecurityCode();
            System.out.println("getSecurityCode::::::" + securityCode);
            securityCodeHidden = securityCode;
            CCType = StringUtils.defaultString(customerDomain.getPaymentMethod().getCreditCardInfo().getCardType().getMeaning());
            ccMntStr = customerDomain.getPaymentMethod().getCreditCardInfo().getExpiryMonth();
            System.out.println("IF ccMntStr::" + ccMntStr);
            ccYrStr = customerDomain.getPaymentMethod().getCreditCardInfo().getExpiryYear();
            System.out.println("IF ccYrStr::" + ccYrStr);
        } else {
            maskCCNo = StringUtils.defaultString(ccInfo.getMaskedCardNumber());
            securityCode = StringUtils.defaultString(ccInfo.getSecurityCode());
            expDt = "NOT";
            ccMntStr = ccInfo.getExpiryDate() != null ? String.valueOf(ccInfo.getExpiryDate().getMonth() + 1) : AsapUtil.getExpiryMonthInNumber(ccExpMonth);
            ccYrStr = ccInfo.getExpiryDate() != null ? String.valueOf(ccInfo.getExpiryDate().getYear()) : ccExpYear;
            maskCCNo = ccnumber;
            CCType = cctypeselect;
            securityCode = ccsecuritycode;
            System.out.println("ELSE ccYrStr::" + ccYrStr);
        }
    } catch (Exception e) {
        System.out.println("Exception in getting values");
    }
%>
<tr>
    <td nowrap="nowrap">
        <select name="ccTypeSelect" id="ccTypeSelect" TABINDEX=19>
            <option value="" selected>Select Type</option>
            <%  for (CreditCardType ccType : EnumSet.allOf(CreditCardType.class)) {
                    if (!CCType.equals("") && CCType.equalsIgnoreCase(ccType.getMeaning())) {
            %>
            <option value="<%=ccType.getMeaning()%>" selected><%=ccType.getMeaning()%>
            </option>
            <%
            } else {
            %>
            <option value="<%=ccType.getMeaning()%>"><%=ccType.getMeaning()%>
            </option>
            <%
                    }
                }
            %>
        </select>
        <!-- turn off autocomplete to fix Bug 119973 PCI - Browser caching autocomplete -->
        <input type="hidden" name="ccTypeSelectHid" id="ccTypeSelectHid" value="<%=CCType%>"/>
        -
        <input name="ccNumber" type="text" id="ccNumber" value="<%=maskCCNo%>" size="18" TABINDEX=20 autocomplete="off"/>
        <input name="ccNumberHid" type="hidden" id="ccNumberHid" value="<%=maskCCNoHidden%>" autocomplete="off"/>
        <input name="ccAccNumberhid" type="hidden" id="ccAccNumberhid" value="<%=newCcAccNumber%>" autocomplete="off"/>
        -
        <input name="ccSecurityCode" type="password" id="ccSecurityCode" value="<%=securityCode%>" size="12"
               TABINDEX=21/>
        <input name="ccSecurityCodeHid" type="hidden" id="ccSecurityCodeHid" value="<%=securityCodeHidden%>" autocomplete="off"/>
    </td>
    <%
        String[] strMonths = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
        String isDateExpired = "false";
        if (!ccYrStr.equals("") && Integer.parseInt(ccYrStr) < (Calendar.getInstance().get(1))) { %>
            <td nowrap="nowrap" class="required">
    <%      isDateExpired = "true";
        } else if (!ccYrStr.equals("") && Integer.parseInt(ccYrStr) == (Calendar.getInstance().get(1))) {
            int month = -1;
            for(int i=0; i<strMonths.length; i++ ) {
                if(AsapUtil.getExpiryMonthInStr(StringUtils.defaultString(ccMntStr)).equalsIgnoreCase(strMonths[i])) {
                    month = i;
                    break;
                }
            }
            if(month < (Calendar.getInstance().get(2))) {
            %>
                <td nowrap="nowrap" class="required">
            <%  isDateExpired = "true";
            } else {
            %>
                <td nowrap="nowrap">
            <%
            }
        } else { %>
                <td nowrap="nowrap">
        <% } %>
        *Expiry Date
    </td>
    <td nowrap="nowrap"><select name="ccMonth" id="ccMonth" TABINDEX=22>
        <option value="">Month</option>
        <%
            for (int i = 0; i < strMonths.length; i++) {
                if (isDateExpired.equals("true")) {
        %>
        <option value="<%=strMonths[i]%>"><%=strMonths[i]%>
        </option>
        <%
        } else {
            if (ccMntStr != null && !"".equals(ccMntStr) && Integer.parseInt(ccMntStr) - 1 == i) {
        %>
        <option value="<%=strMonths[i]%>" selected><%=strMonths[i]%>
        </option>
        <%
        } else {
        %>
        <option value="<%=strMonths[i]%>"><%=strMonths[i]%>
        </option>
        <%
                    }
                }
            }
        %>
    </select>
        <select name="ccYear" id="ccYear" TABINDEX=22>
            <option value="">Year</option>
            <%
                int baseYear = Calendar.getInstance().get(1);
                for (int k = baseYear; k < (baseYear + 15); k++) {
                    if (isDateExpired.equals("true")) {
            %>
            <option value="<%=k%>"><%=k%>
            </option>
            <%
            } else {
            %>
            <%
                if (ccYrStr != null && !"".equals(ccYrStr) && k == Integer.parseInt(ccYrStr)) {
            %>
            <option value="<%=k%>" selected><%=k%>
            </option>
            <%
            } else {
            %>
            <option value="<%=k%>"><%=k%>
            </option>
            <%
                        }
                    }
                }
            %>
        </select>
    </td>
    <input type="hidden" name="ccMonthHid" id="ccMonthHid" value="<%=ccMntStr%>"/>
    <input type="hidden" name="ccYearHid" id="ccYearHid" value="<%=ccYrStr%>"/>
</tr>
<tr>
    <td>
        <input type="hidden" name="ccCheckboxHid" id="ccCheckboxHid" value="<%=ccCheck%>"/>
        <input name="ccCheckbox" type="checkbox" id="ccCheckbox"
               onclick="displayCCAdd(this);" <%=ccCheck%> <%=ccDisable%> TABINDEX=23/>
        Same Name and Address on CC <br></td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
</tr>
<tr>
    <td colspan="6" valign="top">
        <table width="100%" border="0" cellpadding="0" cellspacing="0">
            <tr>
                <td width="100"></td>
                <td>
                    <table id="ccAddressDisp" class="<%=ccClass%>">
                        <tr>
                            <td width="11%" nowrap="nowrap">*Full Name</td>
                            <td width="40%"><input name="ccFullaName" type="text" id="ccFullaName" size="30"
                                                   value="<%=ccFullCustName%>"
                                                   onblur="loadvalue(this);" onkeypress="checkletter(this);" TABINDEX=23
                                                   maxlength="150"/>
                                <input type="hidden" name="ccFullNameHid" id="ccFullNameHid"
                                       value="<%=ccFullCustName%>"/>
                            </td>
                        </tr>
                        <tr>
	                        <!-- turn off autocomplete to fix Bug 119973 PCI - Browser caching autocomplete -->
                            <td nowrap="nowrap">*Address1</td>
                            <td><input name="ccAddress1" type="text" id="ccAddress1" size="30"
                                       value="<%=ccStreet%>"
                                       onblur="loadvalue(this);" onkeypress="checkletter(this);" TABINDEX=24
                                       maxlength="240" autocomplete="off"/>
                                <input type="hidden" name="ccAddress1Hid" id="ccAddress1Hid" value="<%=ccStreet%>"/>
                            </td>
                        </tr>
                        <tr>
                            <td nowrap="nowrap">Address2</td>
                            <td><input name="ccAddress2" type="text" id="ccAddress2" size="30"
                                       value="<%=ccApartName%>"
                                       onblur="loadvalue(this);" onkeypress="checkletter(this);" TABINDEX=25
                                       maxlength="240" autocomplete="off"/>
                                <input type="hidden" name="ccAddress2Hid" id="ccAddress2Hid" value="<%=ccApartName%>"/>
                            </td>
                        </tr>
                        <tr>
                            <td height="31" nowrap="nowrap">*Country</td>
                            <td>
                                <select name="ccCountry" id="ccCountry" value="<%=ccCuntryName%>" TABINDEX=26
                                        onblur="loadvalue(this);" onkeypress="checkletter(this);" style="width:186px">
                                    <option value="">Select</option>
                                    <option value="US" selected>United States</option>
                                </select></td>
                        </tr>
                        <tr>
	                        <!-- turn off autocomplete to fix Bug 119973 PCI - Browser caching autocomplete -->
                            <td nowrap="nowrap">*Zip/Postal Code</td>
                            <td><input name="ccZip" type="text" id="ccZip" size="15"
                                       value="<%=ccZipCode%>"
                                       onblur="getCcCityState();loadvalue(this);" onkeypress="checkletter(this);"
                                       TABINDEX=27 maxlength="60" autocomplete="off"/>
                                <input type="hidden" name="ccZipHid" id="ccZipHid" value="<%=ccZipCode%>"/></td>
                        </tr>
                        <tr>
                            <td nowrap="nowrap">*State/Province</td>
                            <td nowrap="nowrap"><input name="ccState" type="text" id="ccState" size="20"
                                                       value="<%=ccStateName%>"
                                                       onblur="loadvalue(this);" onkeypress="checkletter(this);"
                                                       TABINDEX=28 maxlength="60" autocomplete="off"/>
                                <input type="hidden" name="ccStateHid" id="ccStateHid" value="<%=ccStateName%>"/></td>
                        </tr>
                        <tr>
	                        <!-- turn off autocomplete to fix Bug 119973 PCI - Browser caching autocomplete -->
                            <td nowrap="nowrap">*City</td>
                            <td><input name="ccCity" type="text" id="ccCity" size="20"
                                       value="<%=ccCityName%>"
                                       onblur="loadvalue(this);" onkeypress="checkletter(this);" TABINDEX=29
                                       maxlength="100" autocomplete="off"/>
                                <input type="hidden" name="ccCityHid" id="ccCityHid" value="<%=ccCityName%>"/></td>
                        </tr>
                        <% if (ccSaccAddFlag.equals("N")) { %>
                        <SCRIPT LANGUAGE="JavaScript">
                            document.getElementById("ccCheckbox").checked = false;
                            document.getElementById("ccAddressDisp").className = 'showdisplay';
                            document.getElementById("ccAddress1").value = document.getElementById("ccAddress1Hid").value;
                            var ccAddressH2 = document.getElementById("ccAddress2Hid").value;
                            if (ccAddressH2 == null || ccAddressH2 == 'null') {
                                document.getElementById("ccAddress2").value = '';
                            } else {
                                document.getElementById("ccAddress2").value = ccAddressH2;
                            }
                            var ccCountryElem = document.getElementById("ccCountry");
                            for (var k = 0; k < ccCountryElem.options.length; k++) {
                                if (trim(ccCountryElem.options[k].value) == 'US') {
                                    document.getElementById("ccCountry").selectedIndex = k;
                                }
                            }
                            document.getElementById("ccCity").value = document.getElementById("ccCityHid").value;
                            document.getElementById("ccState").value = document.getElementById("ccStateHid").value;
                            document.getElementById("ccZip").value = document.getElementById("ccZipHid").value;
                            document.getElementById("ccFullaName").value = document.getElementById("ccFullNameHid").value;
                        </script>
                        <% } %>
                    </table>
                </td>
            </tr>
        </table>
    </td>
</tr>
</table>
    <%//UB information display%>
<table width="100%" border="0" style="margin-left:8px;margin-bottom:10px">
    <tr>
        <td class="bold"> UB</td>
    </tr>
    <tr>
        <%
            String ubInvoiceAccountNumber = StringUtils.defaultString(customerDomain.getPaymentMethod().getPaymentAccountNumber());
        %>
        <td>UB Invoice Number: <%=ubInvoiceAccountNumber%>
        </td>
        <input name="customerCare.paymentMethod.ubInvoiceAccountNumber" type="hidden"
               id="customerCare.paymentMethod.ubInvoiceAccountNumber" value="<%=ubInvoiceAccountNumber%>"/>
    </tr>
    <tr>
        <%
            String billPeriod = customerDomain.getPaymentMethod().getUbBillPeriod() != null
                    ? customerDomain.getPaymentMethod().getUbBillPeriod().toString() : "";
        %>
        <td>UB Billing Cycle: <%=billPeriod%>
        </td>
    </tr>
    <%
        //The feature to edit the check box will be available only for Billing Admins
        String disableUBLimitWaiver = "disabled";
        if (AgentRole.isBillingAdmin(user.getEntryAgent().getAgentRole())
                || AgentRole.SMBAdministrator == user.getEntryAgent().getAgentRole()
                || AgentRole.SMBEntryAgent == user.getEntryAgent().getAgentRole()) {
            disableUBLimitWaiver = "";
        }
        String ubLimitWaiver = (customer.getUbLimitWaiver() ? "checked" : "");
        String ubLimitWaiverHid = (customer.getUbLimitWaiver() ? "on" : "");

    %>
    <tr>
        <td><input type="checkbox" name="customerCare.paymentMethod.ubVerifyCheck" onclick="accessCCUBLimitWaiver('<%=disableUBLimitWaiver%>')"
                   id="customerCare.paymentMethod.ubVerifyCheck"/>
            Please verify the customer meets AT&amp;T Credit Policy requirements.
        </td>
    </tr>
    <tr>
        <td>
            <input type="checkbox" name="customerCare.paymentMethod.ubLimitWaiver"
                   id="customerCare.paymentMethod.ubLimitWaiver" <%=ubLimitWaiver%> disabled/>
            <input type="hidden" name="customerCare.paymentMethod.ubLimitWaiverHid"
                   id="customerCare.paymentMethod.ubLimitWaiverHid" value="<%=ubLimitWaiverHid%>"/>
            Override $1000 charge limit.
        </td>
    </tr>
</table>
<%//U-Verse Billing information%>
<table width="100%" border="0" style="margin-left:8px;margin-bottom:10px">
    <tr>
        <td class="bold"> U-verse BAN</td>
    </tr>
    <tr>
        <td><input name="customerCare.paymentMethod.banNumber" type="text" id="customerCare.paymentMethod.banNumber"
                   value="<%=StringUtils.defaultString(banInfo.getBillingAccountNumber())%>" size="18" maxlength="9" <%= disabledBAN %> />
            <input name="customerCare.paymentMethod.banNumberHid" type="hidden" id="customerCare.paymentMethod.banNumberHid" value="<%=oldBanNumber%>"/>
            <input type="checkbox" id="customerCare.paymentMethod.banTaxExemptBox" disabled="disabled"
                    <% if (banInfo.isTaxExempt()) { %>
                   checked="checked"
                    <% } %>
                    /> Tax Exempt
        </td>
    </tr>
    <tr>
        <td><input type="checkbox" name="customerCare.paymentMethod.banCheckbox" id="customerCare.paymentMethod.banCheckbox"/>
            Please verify that you have customer authorization to place charge on their AT&amp;T U-verse bill.
        </td>
    </tr>
</table>
</div>
<div class="marginTopBottom" id="billHis">
</div>
</form>
</div>
<!--End : Payment Method popup-->
<%
    ActiveProduct activeProducts = null;
    List listActProdInfo = (java.util.ArrayList) pageContext.getAttribute("ACTIVE_PROD_LIST", PageContext.REQUEST_SCOPE);
    if (listActProdInfo != null && listActProdInfo.size() > 0) {
        for (int y = 0; y < listActProdInfo.size(); y++) {
            activeProducts = (ActiveProduct) listActProdInfo.get(y);
        }
    }
%>
<!--Start : Active Products popup-->
<div id="minitabscontent2" class="<%=activeProdClass%>" style="MARGIN-LEFT: 8px! important">
    <%
        if (userRole.equals(AgentRole.BillingWHAdministrator)
                || userRole.equals(AgentRole.BillingAdministrator)
                || userRole.equals(AgentRole.SMBAdministrator)) {
    %>
    <div id="inventoryTasksContainer">
        <button id="inventoryTasksBtn" title="Inventory Tasks" onclick="showInventoryTasksPopup()">Inventory Tasks</button>
    </div>
    <%
        }
    %>
    <div id="productTAB" class="billingtable">
        <img src="/OA_MEDIA/xxpluimg/long_loading_bar.gif"/>
    </div>
    <input id="bundleOfferEligibilityAffected" type="hidden" />
</div>

<div class="nodisplay">
    <div id="salesorder"></div>
    <!--End : Active Products popup-->
    <!--Start: Change History Popup for UD-->

    <div id="changehistorypopup">
    </div>
</div>
<!--End : Change History Popuo -->
<!-- Start : Pending Order Added for CR33 -->
<div id="minitabscontent7" class="nodisplay" style="MARGIN-LEFT: 8px! important">
    <div id="pendingOrderTAB" class="billingtable">
        <img src="/OA_MEDIA/xxpluimg/long_loading_bar.gif"/>
    </div>
</div>
<div id="pendingorderpopup"></div>
<!-- End : Pending Order for CR33 -->
<!--Start:Process invoices billing adjustments-->
<div id="minitabscontent5" class="nodisplay" style="MARGIN-LEFT: 8px! important">
    <div id="paymentTAB">
        <div id="paymentsTabLoader" class="billingtable">
            <img src="/OA_MEDIA/xxpluimg/long_loading_bar.gif"/>
        </div>
        <div id="paymentsTabData">
            <%@include file="customerCarePaymentsTab.jsp" %>
        </div>
    </div>
</div>
<!--End:Process invoices billing adjustments-->
<!----------------------------------------CR 33---------------------------------------->
<%
    String actionString = "xxBOOSTibeCancelPendingOrder.jsp?custAcctId=" + custAccountId + "&fromPage=CustomerCare";
    String originType = "PENDING_ORDER_REASON_CODES";
%>

<!----------------------------------------CR 33---------------------------------------->
<!--Start : History popup-->
<div id="minitabscontent3" class="nodisplay" style="MARGIN-LEFT: 8px! important">
    <div id="historyTAB" class="billingtable">
        <img src="/OA_MEDIA/xxpluimg/long_loading_bar.gif"/>
    </div>
</div>
<!--End : History   -->
<!--Start: History - Activity details popup-->
<div id="activityPopUp">
    <div class="smallpopup">

        <div>
            <table width="100%" border="0">
                <tr>
                    <td colspan="2">
                        <div id="actDtl">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td width="37%">&nbsp;</td>
                    <td width="63%" height="44" nowrap="nowrap">
                        <img name="imageField"
                             src="/OA_MEDIA/xxpluimg/close_button.gif"
                             onclick="javascript:hideActDetails()"
                             onMouseOver="changeImgMouseOver(this,this.src);"
                             onMouseOut="changeImgMouseOut(this,this.src);"/>
                    </td>
                </tr>
            </table>
        </div>
    </div>
</div>
<!--End: History activity details popup -->
<!--Start: Authorised Users-->
<div id="minitabscontent4" style="MARGIN-LEFT: 4px! important" class="<%=authUserClass%>">
    <div>
    <form name="authUser" method="post" id="authUser">

        <table cellspacing="0" cellpadding="0" border="0">
            <tbody>
            <tr>
                <td valign="top" class="bold">New Authorized User</td>
                <td valign="top">
                    <input name="EmailAuth" type="text" id="EmailAuth" value="<%=emailauth%>"
                           onfocus="clearText(this);" onblur="chkNull(this);loadvalue(this);"
                           onkeypress="checkletter(this);" maxlength="60"/>
                </td>
                <td rowspan="2" valign="top">
                    <table width="100%" border="0" cellpadding="0" cellspacing="0">
                        <tr>
                            <td>
                                <input name="FnameAuth" type="text" id="FnameAuth" value="<%=fnameauth%>"
                                       onfocus="clearText(this);loadvalue(this);" onblur="chkNull(this);"
                                       onkeypress="checkletter(this);" maxlength="60"/>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <input name="LnameAuth" type="text" id="LnameAuth" value="<%=lnameauth%>"
                                       onfocus="clearText(this);" onblur="chkNull(this);loadvalue(this);"
                                       onkeypress="checkletter(this);" maxlength="60"/>
                            </td>
                        </tr>

                    </table>
                </td>

                <td rowspan="2" valign="top">
                    <table width="100%" border="0" cellpadding="0" cellspacing="0">
                        <tr>
                            <td valign="top">
                                <input name="PhoneAuth" type="text" id="PhoneAuth" value="<%=phoneauth%>"
                                       onfocus="clearText(this);" onblur="chkNull(this);loadvalue(this);" maxlength="20"
                                       onkeypress="checkletter(this);" maxlength="60"/>
                            </td>
                        </tr>
                        <tr>
                            <td valign="top">
                                <select name="AuthRole" id="AuthRole">
                                    <option value="">*Select Role</option>
                                    <%
                                        UIConfig roleList = new UIConfig();
                                        for (int k = 0; k < roleTypeLookup.size(); k++) {
                                            roleList = (UIConfig) roleTypeLookup.get(k);
                                    %>
                                    <option value="<%=roleList.getConfig()%>"><%=roleList.getConfig()%>
                                    </option>
                                    <%
                                        }
                                    %>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td></td>
                <td></td>
            </tr>
            <tr>
                <td height="33"></td>
                <td></td>
                <td></td>
                <td>
                    <input type="button" onMouseOver="changeImgMouseOver(this,this.src);"
                           onclick="displayAuthorisedUsers('AddAuthorizedUsersBTN');"
                           onMouseOut="changeImgMouseOut(this,this.src);" name="AddAuthorizedUsersBTN"
                           id="AddAuthorizedUsersBTN" value="Add Authorized User"/>
                </td>
            </tr>
            </tbody>
        </table>
        <div class="marginTopBottom">
            <div id="userTAB" class="container">
            </div>
        </div>
        <input name="customerId" type="hidden" id="customerId" value="<%=custAccountId%>"/>
    </form>
    </div>

</div>
<!--End: Authorised Users-->

<!-- Begin Create One Time Invoice Screen -->
<div id="oneTimeInvoiceDisplay"></div>
<!-- End Create One Time Invoice screen -->

<!--Start: Task-->
<form name="frm1" id="frm1" method="post" ACTION="">
    <input name="checkselect" type="hidden" id="checkselect"/>
    <input name="addressParams" type="hidden" id="addressParams"/>
    <%if (request.getParameter("checkselect") != null && request.getParameter("checkselect").equals("Y")) {%>
        <div id="minitabscontent6" style="MARGIN-LEFT: 8px! important">
    <%} else {%>
        <div id="minitabscontent6" class="nodisplay" style="MARGIN-LEFT: 8px! important">
    <%}%>
            <div id="taskTAB" class="billingtable">
                <img src="/OA_MEDIA/xxpluimg/long_loading_bar.gif"/>
            </div>
        </div>
</form>
<!--End: Task-->

<div class="buttoncotainer">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
            <td width="84%">&nbsp;</td>
            <td width="16%"><img style="cursor:hand" name="save2" id="save2"
                                 src="/OA_MEDIA/xxpluimg/save_changes_button.gif" onclick="showSaveChanges();"
                                 onMouseOver="changeImgMouseOver(this,this.src);"
                                 onMouseOut="changeImgMouseOut(this,this.src);" TABINDEX=30/></td>
        </tr>
    </table>
</div>

<div class="nodisplay">
    <%@include file="changeAccountStatusPopup.jsp" %>
    <%@include file="updateAuthUsersPopup.jsp" %>
    <%@include file="returnProductPopup.jsp" %>
</div>

<!-- @Mission: CR159, add Resubmit Unpaid Invoices -->
<!-- @Author: Shenglin Qiu -->
<!-- @Date: Nov-17-2010 -->
<div id='unpaidInvoicesWrapper'>
    <form name="resubmitUnpaidInvoicesForm" id="resubmitUnpaidInvoicesForm">

        <div id='unpaidinvoices' class="smallpopupUnpaidInvoices">
            <div class="unpaidInvoiceLoadingCircle">
                <img alt="loading..." src="/OA_MEDIA/xxpluimg/loadingCircle.gif"/>
            </div>
        </div>
    </form>

</div>
<!-- End of Resubmit Unpaid Invoices -->
<div class="nodisplay">
    <!-- End Schedule link-->
    <!-- Start: Reason Code Pop-up  -->
    <div id="RefundReturnpopupwrapper">
        <form name="reasonCodeForm" method="post" ACTION="">

            <div>

                <p class="subhead" id="desc"></p>

                <div>
                    <table width="100%" border="0">
                        <tr>
                            <td colspan="2">
                                <table width="100%" border="0" cellpadding="2" cellspacing="2">
                                    <tr>
                                        <td nowrap="nowrap"> Select Reason Code</td>
                                        <td><select name="refundReasonCode" id="refundReasonCode">
                                            <%
                                                if (productReasonCodes.size() > 0) {
                                                    for (int i = 0; i < productReasonCodes.size(); i++) {
                                                        productReasonValues = (UIConfig) productReasonCodes.get(i);
                                                        if (StringUtils.defaultString(request.getParameter("refundReasonCode")).equals(productReasonValues.getParent())) {
                                            %>
                                            <option value="<%=productReasonValues.getConfig()%>"
                                                    selected><%=productReasonValues.getConfig()%>
                                            </option>
                                            <%
                                            } else {
                                            %>
                                            <option value="<%=productReasonValues.getConfig()%>"><%=productReasonValues.getConfig()%>
                                            </option>
                                            <%
                                                        }
                                                    }
                                                }
                                            %>
                                        </select></td>
                                    </tr>
                                    <tr>
                                        <td nowrap="nowrap">ETF Amount</td>
                                        <td height='7' style='border:0px solid;' nowrap="nowrap">
                                            <input name='etf' type='text' id='etf' size='6' align='center'/>
                                            <label id="freeDomRefNote" class="nodisplay"> (Inclusive of 1yr Free Domain
                                                Registration Amount)</label>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                        <tr>
                            <td width="27%">&nbsp;</td>
                            <td width="73%" height="44" nowrap="nowrap">
                                <a href="javascript:hideRefundReasonCodes();">
                                    <img name="cancelRefundBtn"
                                         id="cancelRefundBtn"
                                         src="/OA_MEDIA/xxpluimg/cancel_button.gif"
                                         border="0"/></a>&nbsp;&nbsp;&nbsp;&nbsp;
                                <a href="javascript:processRefund();">
                                    <img name="processRefundBtn" id="processRefundBtn"
                                         src="/OA_MEDIA/xxpluimg/apply_button.gif"
                                         border="0"/></a></td>
                        </tr>
                    </table>
                </div>
            </div>
        </form>
    </div>
</div>
<!-- End : Reason Code Pop-up -->
<!-- Black list popup start here  15JAN2010-->
<div id="BlackListpopup">
    <div class="smallpopup">
        <h3>Security Alert</h3>

        <div>
            <table width="100%" border="0">
                <tr>
                    <td colspan="2">
                        <table width="100%" border="0" cellpadding="2" cellspacing="2">
                            <tr>
                                Orders from this country cannot be accepted by the system.
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td width="27%">&nbsp;</td>
                    <td width="73%" height="10" nowrap="nowrap">
                        <img style="cursor:hand" name="imageField10" src="/OA_MEDIA/xxpluimg/cancel_button.gif"
                             onclick="javascript:hideBlackListCountry()"
                             onMouseOver="changeImgMouseOver(this,this.src);"
                             onMouseOut="changeImgMouseOut(this,this.src);"/></td>
                </tr>
            </table>
        </div>
    </div>
</div>

<div id="custnotes" class="notesPopup">
    <form name='notesForm' method='post' ACTION="">
        <div>
<!-- Changed by Nimanthi to make the popup compatible with IE8 and IE10 -->
            <div id="serverNotesError"></div>
            <div>
           		<label for='title'>Title</label>
           		<input name='title' id='title' type='text' size='45' value='' maxlength="99"/>
           		<br />
           		<br />
           		<textarea name='notes' id='notes' cols='40' rows='5' style='overflow:hidden' value=''
                                  onkeyPress="return(document.notesForm.notes.value.length < 999)"></textarea>
           		<br />
           		<input name='salesAgent' id='salesAgent' type='hidden' value="<%=userName%>"/> 
           		<br />
           		<div width='80%' align="center">
            		<a href="javascript:showNotesDetails();" style="text-decoration:none;">
            			<img name="customerCare.customerDetails.notes.showBTN" id="customerCare.customerDetails.notes.showBTN"
							 src="/OA_MEDIA/xxpluimg/ok_button.gif" border="0"/>
					</a>
					<a href="javascript:hideNotesPopup();" style="text-decoration:none; padding-left:25px;">
						<img name="customerCare.customerDetails.notes.cancelBTN" id="customerCare.customerDetails.notes.cancelBTN"
							 src="/OA_MEDIA/xxpluimg/cancel_button.gif" border="0"/>
					</a>
				</div>
			</div>
        </div>
    </form>
</div>

<!-- Black list popup end here -->
<div id="cover2"></div>

<div id="billAdjustPopup">
    <form name='formBillAdjust' method='post'>
        <input type="hidden" name="etfTemp" id="etfTemp"/>

        <div id="billadjust"></div>
    </form>
</div>
</td>
</tr>

<% if ("Y".equals(blackListFlag)) { %>
<script>showBlackListCountry();</script>
<%}%>

<%@include file="xxBOOSTFooter.jsp" %>
<script type="text/javascript">
    //    unMaskCustDetails();
    <%if (!("").equals(tab4Class)) {%>
    window.onload = callUserTab;
    <%
    }
    if (!("").equals(tab6Class)) {
    %>
    window.onload = callTaskTab;
    <%
    }
    if (!("").equals(tab2Class)) {
    %>
    window.onload = callProductsTab;
    <%}%>
    $(function () {
        initDialog("RefundReturnpopupwrapper", null, "Refund Reason Code");
        initDialog("billAdjustPopup", 768);
        initDialog("changehistorypopup", null, "Product Change History");
        initDialog("salesorder");
        initDialog("pendingorderpopup");
        initDialog("activityPopUp", 480, "Activity Details");
        initDialog('unpaidInvoicesWrapper', null);
        initDialog("custnotes", 350, "Notes");
        initDialog("BlackListpopup", null, "Security Alert");
        initDialog("oneTimeInvoiceDisplay", 650);
        closeDialog("pageLoadingCircle");

    });
</script>
</table>
<div class="nodisplay">

    <div>
        <!-- Begin Modify Product Screen -->
        <%@include file="xxasapModifyProduct.jsp" %>
        <!-- End Modify Product screen -->
    </div>
    <div id="productTaskPopup">
        <!-- Start Product Tasks link-->
        <%@include file="xxasapProductTask.jsp" %>
        <!-- End Product Tasks link-->
    </div>

    <div>
        <!-- Start Invoice Copy request Pop-up -->
        <%@include file="xxAjaxInvoicePopup.jsp" %>
        <!-- End Invoice Copy request Pop-up -->
    </div>

    <div>
        <%@include file="changeProductWH.jsp" %>
    </div>

    <div>
        <%@include file="changeProductCalculations.jsp" %>
    </div>

    <div id="BEPopup">
        <%@include file="whConfigPopupForUpDown.jsp" %>
    </div>
    <div>
        <%@include file="changeProductAddons.jsp" %>
    </div>
    <div>
        <%@include file="invoicePopup.jsp" %>
    </div>
    <div>
        <%@include file="inventoryTasksPopup.jsp" %>
    </div>
    <div>
        <%@include file="productTaskDiscountEligibilityMessagePopup.jsp" %>
    </div>
    <div>
        <%@include file="lossFlexibleBundleEligibilityPopup.jsp" %>
    </div>
    <div id="TldForUpDownPopup">
        <%@include file="tldPopup.jsp" %>
    </div>

    <jsp:include page="xxBOOSTRescheduleAppoint.jsp"/>
    <jsp:include page="xxBOOSTCustDetailsTaskPopup.jsp"/>
    <jsp:include page="xxBOOSTibePendingOrderReasonCode.jsp" flush="true">
        <jsp:param name="actionString" value="<%=actionString%>"/>
        <jsp:param name="originType" value="<%=originType%>"/>
    </jsp:include>
    <!-- Start Schedule link-->
    <%
        String linkScheduleAppt = "xxASAPLinkSchAppoint.jsp?custid=" + custAccountId + "&subMenu=" + subMenu + "&sbsproduct=" + sbsproduct + "&customerno=" + customerno + "&EmailaddR=" + emailr + "&PhoneR=" + phoner + "&FnameR=" + fnamer + "&CityR=" + cityr + "&CompanyR=" + companyr + "&LnameR=" + lnamer + "&CountryR=" + countryr + "&dispbutton=" + dispbutton + "&invoice=" + invoice + "&btn=" + btn + "&cc=" + cc + "&ccMerchant=" + ccMerchant + "&order=" + order + "&ticket=" + ticket + "&domain=" + domain + "&fannum=" + fannum + "&docviewid=" + docviewid + "&inventoryid=" + inventoryid + "&searchBy=" + searchBy + "&fromBackButton=Y";
    %>
    <jsp:include page="<%=linkScheduleAppt%>"/>
</div>

<div id="cover1"></div>
<!-- Main wrapper Table End here -->
</body>
</html>
