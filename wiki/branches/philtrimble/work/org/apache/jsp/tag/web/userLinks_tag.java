package org.apache.jsp.tag.web;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class userLinks_tag
    extends javax.servlet.jsp.tagext.SimpleTagSupport
    implements org.apache.jasper.runtime.JspSourceDependent {


  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  private JspContext jspContext;
  private java.io.Writer _jspx_sout;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005ffmt_005fsetBundle_0026_005fbasename_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvalue_005ftext_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvalue;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fjamwiki_005flinkParam_0026_005fvalue_005fkey_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fsecurity_005fauthorize_0026_005furl;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.AnnotationProcessor _jsp_annotationprocessor;

  public void setJspContext(JspContext ctx) {
    super.setJspContext(ctx);
    java.util.ArrayList _jspx_nested = null;
    java.util.ArrayList _jspx_at_begin = null;
    java.util.ArrayList _jspx_at_end = null;
    this.jspContext = new org.apache.jasper.runtime.JspContextWrapper(ctx, _jspx_nested, _jspx_at_begin, _jspx_at_end, null);
  }

  public JspContext getJspContext() {
    return this.jspContext;
  }
  private org.jamwiki.servlets.WikiPageInfo pageInfo;
  private java.lang.String userDisplay;

  public org.jamwiki.servlets.WikiPageInfo getPageInfo() {
    return this.pageInfo;
  }

  public void setPageInfo(org.jamwiki.servlets.WikiPageInfo pageInfo) {
    this.pageInfo = pageInfo;
    jspContext.setAttribute("pageInfo", pageInfo);
  }

  public java.lang.String getUserDisplay() {
    return this.userDisplay;
  }

  public void setUserDisplay(java.lang.String userDisplay) {
    this.userDisplay = userDisplay;
    jspContext.setAttribute("userDisplay", userDisplay);
  }

  public Object getDependants() {
    return _jspx_dependants;
  }

  private void _jspInit(ServletConfig config) {
    _005fjspx_005ftagPool_005ffmt_005fsetBundle_0026_005fbasename_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(config);
    _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvalue_005ftext_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(config);
    _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvalue = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(config);
    _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(config);
    _005fjspx_005ftagPool_005fjamwiki_005flinkParam_0026_005fvalue_005fkey_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(config);
    _005fjspx_005ftagPool_005fsecurity_005fauthorize_0026_005furl = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(config);
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(config.getServletContext()).getExpressionFactory();
    _jsp_annotationprocessor = (org.apache.AnnotationProcessor) config.getServletContext().getAttribute(org.apache.AnnotationProcessor.class.getName());
  }

  public void _jspDestroy() {
    _005fjspx_005ftagPool_005ffmt_005fsetBundle_0026_005fbasename_005fnobody.release();
    _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvalue_005ftext_005fnobody.release();
    _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvalue.release();
    _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.release();
    _005fjspx_005ftagPool_005fjamwiki_005flinkParam_0026_005fvalue_005fkey_005fnobody.release();
    _005fjspx_005ftagPool_005fsecurity_005fauthorize_0026_005furl.release();
  }

  public void doTag() throws JspException, java.io.IOException {
    PageContext _jspx_page_context = (PageContext)jspContext;
    HttpServletRequest request = (HttpServletRequest) _jspx_page_context.getRequest();
    HttpServletResponse response = (HttpServletResponse) _jspx_page_context.getResponse();
    HttpSession session = _jspx_page_context.getSession();
    ServletContext application = _jspx_page_context.getServletContext();
    ServletConfig config = _jspx_page_context.getServletConfig();
    JspWriter out = jspContext.getOut();
    _jspInit(config);
    jspContext.getELContext().putContext(JspContext.class,jspContext);
    if( getPageInfo() != null ) 
      _jspx_page_context.setAttribute("pageInfo", getPageInfo());
    if( getUserDisplay() != null ) 
      _jspx_page_context.setAttribute("userDisplay", getUserDisplay());

    try {
      if (_jspx_meth_fmt_005fsetBundle_005f0(_jspx_page_context))
        return;
      if (_jspx_meth_jamwiki_005flink_005f0(_jspx_page_context))
        return;
      out.write('\r');
      out.write('\n');
      out.write('(');
      if (_jspx_meth_jamwiki_005flink_005f1(_jspx_page_context))
        return;
      out.write("&#160;|&#160;");
      if (_jspx_meth_jamwiki_005flink_005f2(_jspx_page_context))
        return;
      if (_jspx_meth_security_005fauthorize_005f0(_jspx_page_context))
        return;
      out.write(')');
      out.write('\r');
      out.write('\n');
    } catch( Throwable t ) {
      if( t instanceof SkipPageException )
          throw (SkipPageException) t;
      if( t instanceof java.io.IOException )
          throw (java.io.IOException) t;
      if( t instanceof IllegalStateException )
          throw (IllegalStateException) t;
      if( t instanceof JspException )
          throw (JspException) t;
      throw new JspException(t);
    } finally {
      jspContext.getELContext().putContext(JspContext.class,super.getJspContext());
      ((org.apache.jasper.runtime.JspContextWrapper) jspContext).syncEndTagFile();
      _jspDestroy();
    }
  }

  private boolean _jspx_meth_fmt_005fsetBundle_005f0(PageContext _jspx_page_context)
          throws Throwable {
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:setBundle
    org.apache.taglibs.standard.tag.rt.fmt.SetBundleTag _jspx_th_fmt_005fsetBundle_005f0 = (org.apache.taglibs.standard.tag.rt.fmt.SetBundleTag) _005fjspx_005ftagPool_005ffmt_005fsetBundle_0026_005fbasename_005fnobody.get(org.apache.taglibs.standard.tag.rt.fmt.SetBundleTag.class);
    _jspx_th_fmt_005fsetBundle_005f0.setPageContext(_jspx_page_context);
    _jspx_th_fmt_005fsetBundle_005f0.setParent(new javax.servlet.jsp.tagext.TagAdapter((javax.servlet.jsp.tagext.SimpleTag) this ));    // /WEB-INF/tags/userLinks.tag(9,0) name = basename type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_fmt_005fsetBundle_005f0.setBasename("ApplicationResources");
    int _jspx_eval_fmt_005fsetBundle_005f0 = _jspx_th_fmt_005fsetBundle_005f0.doStartTag();
    if (_jspx_th_fmt_005fsetBundle_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005ffmt_005fsetBundle_0026_005fbasename_005fnobody.reuse(_jspx_th_fmt_005fsetBundle_005f0);
      throw new SkipPageException();
    }
    _005fjspx_005ftagPool_005ffmt_005fsetBundle_0026_005fbasename_005fnobody.reuse(_jspx_th_fmt_005fsetBundle_005f0);
    return false;
  }

  private boolean _jspx_meth_jamwiki_005flink_005f0(PageContext _jspx_page_context)
          throws Throwable {
    JspWriter out = _jspx_page_context.getOut();
    //  jamwiki:link
    org.jamwiki.taglib.LinkTag _jspx_th_jamwiki_005flink_005f0 = (org.jamwiki.taglib.LinkTag) _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvalue_005ftext_005fnobody.get(org.jamwiki.taglib.LinkTag.class);
    _jspx_th_jamwiki_005flink_005f0.setPageContext(_jspx_page_context);
    _jspx_th_jamwiki_005flink_005f0.setParent(new javax.servlet.jsp.tagext.TagAdapter((javax.servlet.jsp.tagext.SimpleTag) this ));    // /WEB-INF/tags/userLinks.tag(14,0) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005flink_005f0.setValue((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageInfo.namespaces[pageInfo.virtualWikiName]['User']}:${userDisplay}", java.lang.String.class, (PageContext)this.getJspContext(), null, false));
    // /WEB-INF/tags/userLinks.tag(14,0) name = text type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005flink_005f0.setText((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${userDisplay}", java.lang.String.class, (PageContext)this.getJspContext(), null, false));
    int _jspx_eval_jamwiki_005flink_005f0 = _jspx_th_jamwiki_005flink_005f0.doStartTag();
    if (_jspx_th_jamwiki_005flink_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvalue_005ftext_005fnobody.reuse(_jspx_th_jamwiki_005flink_005f0);
      throw new SkipPageException();
    }
    _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvalue_005ftext_005fnobody.reuse(_jspx_th_jamwiki_005flink_005f0);
    return false;
  }

  private boolean _jspx_meth_jamwiki_005flink_005f1(PageContext _jspx_page_context)
          throws Throwable {
    JspWriter out = _jspx_page_context.getOut();
    //  jamwiki:link
    org.jamwiki.taglib.LinkTag _jspx_th_jamwiki_005flink_005f1 = (org.jamwiki.taglib.LinkTag) _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvalue.get(org.jamwiki.taglib.LinkTag.class);
    _jspx_th_jamwiki_005flink_005f1.setPageContext(_jspx_page_context);
    _jspx_th_jamwiki_005flink_005f1.setParent(new javax.servlet.jsp.tagext.TagAdapter((javax.servlet.jsp.tagext.SimpleTag) this ));    // /WEB-INF/tags/userLinks.tag(15,1) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005flink_005f1.setValue((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageInfo.namespaces[pageInfo.virtualWikiName]['User comments']}:${userDisplay}", java.lang.String.class, (PageContext)this.getJspContext(), null, false));
    int _jspx_eval_jamwiki_005flink_005f1 = _jspx_th_jamwiki_005flink_005f1.doStartTag();
    if (_jspx_eval_jamwiki_005flink_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_jamwiki_005flink_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_jamwiki_005flink_005f1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_jamwiki_005flink_005f1.doInitBody();
      }
      do {
        if (_jspx_meth_fmt_005fmessage_005f0(_jspx_th_jamwiki_005flink_005f1, _jspx_page_context))
          return true;
        int evalDoAfterBody = _jspx_th_jamwiki_005flink_005f1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_jamwiki_005flink_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.popBody();
      }
    }
    if (_jspx_th_jamwiki_005flink_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvalue.reuse(_jspx_th_jamwiki_005flink_005f1);
      throw new SkipPageException();
    }
    _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvalue.reuse(_jspx_th_jamwiki_005flink_005f1);
    return false;
  }

  private boolean _jspx_meth_fmt_005fmessage_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_jamwiki_005flink_005f1, PageContext _jspx_page_context)
          throws Throwable {
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_005fmessage_005f0 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_005fmessage_005f0.setPageContext(_jspx_page_context);
    _jspx_th_fmt_005fmessage_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_jamwiki_005flink_005f1);
    // /WEB-INF/tags/userLinks.tag(15,104) name = key type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_fmt_005fmessage_005f0.setKey("recentchanges.caption.comments");
    int _jspx_eval_fmt_005fmessage_005f0 = _jspx_th_fmt_005fmessage_005f0.doStartTag();
    if (_jspx_th_fmt_005fmessage_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f0);
      throw new SkipPageException();
    }
    _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f0);
    return false;
  }

  private boolean _jspx_meth_jamwiki_005flink_005f2(PageContext _jspx_page_context)
          throws Throwable {
    JspWriter out = _jspx_page_context.getOut();
    //  jamwiki:link
    org.jamwiki.taglib.LinkTag _jspx_th_jamwiki_005flink_005f2 = (org.jamwiki.taglib.LinkTag) _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvalue.get(org.jamwiki.taglib.LinkTag.class);
    _jspx_th_jamwiki_005flink_005f2.setPageContext(_jspx_page_context);
    _jspx_th_jamwiki_005flink_005f2.setParent(new javax.servlet.jsp.tagext.TagAdapter((javax.servlet.jsp.tagext.SimpleTag) this ));    // /WEB-INF/tags/userLinks.tag(15,184) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005flink_005f2.setValue("Special:Contributions");
    int _jspx_eval_jamwiki_005flink_005f2 = _jspx_th_jamwiki_005flink_005f2.doStartTag();
    if (_jspx_eval_jamwiki_005flink_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_jamwiki_005flink_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_jamwiki_005flink_005f2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_jamwiki_005flink_005f2.doInitBody();
      }
      do {
        if (_jspx_meth_jamwiki_005flinkParam_005f0(_jspx_th_jamwiki_005flink_005f2, _jspx_page_context))
          return true;
        if (_jspx_meth_fmt_005fmessage_005f1(_jspx_th_jamwiki_005flink_005f2, _jspx_page_context))
          return true;
        int evalDoAfterBody = _jspx_th_jamwiki_005flink_005f2.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_jamwiki_005flink_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.popBody();
      }
    }
    if (_jspx_th_jamwiki_005flink_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvalue.reuse(_jspx_th_jamwiki_005flink_005f2);
      throw new SkipPageException();
    }
    _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvalue.reuse(_jspx_th_jamwiki_005flink_005f2);
    return false;
  }

  private boolean _jspx_meth_jamwiki_005flinkParam_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_jamwiki_005flink_005f2, PageContext _jspx_page_context)
          throws Throwable {
    JspWriter out = _jspx_page_context.getOut();
    //  jamwiki:linkParam
    org.jamwiki.taglib.LinkParamTag _jspx_th_jamwiki_005flinkParam_005f0 = (org.jamwiki.taglib.LinkParamTag) _005fjspx_005ftagPool_005fjamwiki_005flinkParam_0026_005fvalue_005fkey_005fnobody.get(org.jamwiki.taglib.LinkParamTag.class);
    _jspx_th_jamwiki_005flinkParam_005f0.setPageContext(_jspx_page_context);
    _jspx_th_jamwiki_005flinkParam_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_jamwiki_005flink_005f2);
    // /WEB-INF/tags/userLinks.tag(15,228) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005flinkParam_005f0.setKey("contributor");
    // /WEB-INF/tags/userLinks.tag(15,228) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005flinkParam_005f0.setValue((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${userDisplay}", java.lang.String.class, (PageContext)this.getJspContext(), null, false));
    int _jspx_eval_jamwiki_005flinkParam_005f0 = _jspx_th_jamwiki_005flinkParam_005f0.doStartTag();
    if (_jspx_th_jamwiki_005flinkParam_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fjamwiki_005flinkParam_0026_005fvalue_005fkey_005fnobody.reuse(_jspx_th_jamwiki_005flinkParam_005f0);
      throw new SkipPageException();
    }
    _005fjspx_005ftagPool_005fjamwiki_005flinkParam_0026_005fvalue_005fkey_005fnobody.reuse(_jspx_th_jamwiki_005flinkParam_005f0);
    return false;
  }

  private boolean _jspx_meth_fmt_005fmessage_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_jamwiki_005flink_005f2, PageContext _jspx_page_context)
          throws Throwable {
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_005fmessage_005f1 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_005fmessage_005f1.setPageContext(_jspx_page_context);
    _jspx_th_fmt_005fmessage_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_jamwiki_005flink_005f2);
    // /WEB-INF/tags/userLinks.tag(15,290) name = key type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_fmt_005fmessage_005f1.setKey("recentchanges.caption.contributions");
    int _jspx_eval_fmt_005fmessage_005f1 = _jspx_th_fmt_005fmessage_005f1.doStartTag();
    if (_jspx_th_fmt_005fmessage_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f1);
      throw new SkipPageException();
    }
    _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f1);
    return false;
  }

  private boolean _jspx_meth_security_005fauthorize_005f0(PageContext _jspx_page_context)
          throws Throwable {
    JspWriter out = _jspx_page_context.getOut();
    //  security:authorize
    org.springframework.security.taglibs.authz.JspAuthorizeTag _jspx_th_security_005fauthorize_005f0 = (org.springframework.security.taglibs.authz.JspAuthorizeTag) _005fjspx_005ftagPool_005fsecurity_005fauthorize_0026_005furl.get(org.springframework.security.taglibs.authz.JspAuthorizeTag.class);
    _jspx_th_security_005fauthorize_005f0.setPageContext(_jspx_page_context);
    _jspx_th_security_005fauthorize_005f0.setParent(new javax.servlet.jsp.tagext.TagAdapter((javax.servlet.jsp.tagext.SimpleTag) this ));    // /WEB-INF/tags/userLinks.tag(15,362) name = url type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_security_005fauthorize_005f0.setUrl("/Special:Block");
    int _jspx_eval_security_005fauthorize_005f0 = _jspx_th_security_005fauthorize_005f0.doStartTag();
    if (_jspx_eval_security_005fauthorize_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      out.write("&#160;|&#160;");
      if (_jspx_meth_jamwiki_005flink_005f3(_jspx_th_security_005fauthorize_005f0, _jspx_page_context))
        return true;
    }
    if (_jspx_th_security_005fauthorize_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fsecurity_005fauthorize_0026_005furl.reuse(_jspx_th_security_005fauthorize_005f0);
      throw new SkipPageException();
    }
    _005fjspx_005ftagPool_005fsecurity_005fauthorize_0026_005furl.reuse(_jspx_th_security_005fauthorize_005f0);
    return false;
  }

  private boolean _jspx_meth_jamwiki_005flink_005f3(javax.servlet.jsp.tagext.JspTag _jspx_th_security_005fauthorize_005f0, PageContext _jspx_page_context)
          throws Throwable {
    JspWriter out = _jspx_page_context.getOut();
    //  jamwiki:link
    org.jamwiki.taglib.LinkTag _jspx_th_jamwiki_005flink_005f3 = (org.jamwiki.taglib.LinkTag) _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvalue.get(org.jamwiki.taglib.LinkTag.class);
    _jspx_th_jamwiki_005flink_005f3.setPageContext(_jspx_page_context);
    _jspx_th_jamwiki_005flink_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_security_005fauthorize_005f0);
    // /WEB-INF/tags/userLinks.tag(15,416) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005flink_005f3.setValue("Special:Block");
    int _jspx_eval_jamwiki_005flink_005f3 = _jspx_th_jamwiki_005flink_005f3.doStartTag();
    if (_jspx_eval_jamwiki_005flink_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_jamwiki_005flink_005f3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_jamwiki_005flink_005f3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_jamwiki_005flink_005f3.doInitBody();
      }
      do {
        if (_jspx_meth_jamwiki_005flinkParam_005f1(_jspx_th_jamwiki_005flink_005f3, _jspx_page_context))
          return true;
        if (_jspx_meth_fmt_005fmessage_005f2(_jspx_th_jamwiki_005flink_005f3, _jspx_page_context))
          return true;
        int evalDoAfterBody = _jspx_th_jamwiki_005flink_005f3.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_jamwiki_005flink_005f3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.popBody();
      }
    }
    if (_jspx_th_jamwiki_005flink_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvalue.reuse(_jspx_th_jamwiki_005flink_005f3);
      throw new SkipPageException();
    }
    _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvalue.reuse(_jspx_th_jamwiki_005flink_005f3);
    return false;
  }

  private boolean _jspx_meth_jamwiki_005flinkParam_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_jamwiki_005flink_005f3, PageContext _jspx_page_context)
          throws Throwable {
    JspWriter out = _jspx_page_context.getOut();
    //  jamwiki:linkParam
    org.jamwiki.taglib.LinkParamTag _jspx_th_jamwiki_005flinkParam_005f1 = (org.jamwiki.taglib.LinkParamTag) _005fjspx_005ftagPool_005fjamwiki_005flinkParam_0026_005fvalue_005fkey_005fnobody.get(org.jamwiki.taglib.LinkParamTag.class);
    _jspx_th_jamwiki_005flinkParam_005f1.setPageContext(_jspx_page_context);
    _jspx_th_jamwiki_005flinkParam_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_jamwiki_005flink_005f3);
    // /WEB-INF/tags/userLinks.tag(15,452) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005flinkParam_005f1.setKey("user");
    // /WEB-INF/tags/userLinks.tag(15,452) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005flinkParam_005f1.setValue((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${userDisplay}", java.lang.String.class, (PageContext)this.getJspContext(), null, false));
    int _jspx_eval_jamwiki_005flinkParam_005f1 = _jspx_th_jamwiki_005flinkParam_005f1.doStartTag();
    if (_jspx_th_jamwiki_005flinkParam_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fjamwiki_005flinkParam_0026_005fvalue_005fkey_005fnobody.reuse(_jspx_th_jamwiki_005flinkParam_005f1);
      throw new SkipPageException();
    }
    _005fjspx_005ftagPool_005fjamwiki_005flinkParam_0026_005fvalue_005fkey_005fnobody.reuse(_jspx_th_jamwiki_005flinkParam_005f1);
    return false;
  }

  private boolean _jspx_meth_fmt_005fmessage_005f2(javax.servlet.jsp.tagext.JspTag _jspx_th_jamwiki_005flink_005f3, PageContext _jspx_page_context)
          throws Throwable {
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_005fmessage_005f2 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_005fmessage_005f2.setPageContext(_jspx_page_context);
    _jspx_th_fmt_005fmessage_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_jamwiki_005flink_005f3);
    // /WEB-INF/tags/userLinks.tag(15,507) name = key type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_fmt_005fmessage_005f2.setKey("recentchanges.caption.block");
    int _jspx_eval_fmt_005fmessage_005f2 = _jspx_th_fmt_005fmessage_005f2.doStartTag();
    if (_jspx_th_fmt_005fmessage_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f2);
      throw new SkipPageException();
    }
    _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f2);
    return false;
  }
}
