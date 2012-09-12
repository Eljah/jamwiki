package org.apache.jsp.WEB_002dINF.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class topic_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  static {
    _jspx_dependants = new java.util.ArrayList(5);
    _jspx_dependants.add("/WEB-INF/jsp/page-init.jsp");
    _jspx_dependants.add("/WEB-INF/jsp/view-topic-include.jsp");
    _jspx_dependants.add("/WEB-INF/jsp/view-category-include.jsp");
    _jspx_dependants.add("/WEB-INF/jsp/category-include.jsp");
    _jspx_dependants.add("/WEB-INF/tags/userLinks.tag");
  }

  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005ffmt_005fsetBundle_0026_005fbasename_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fif_0026_005ftest;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005ffmt_005fparam;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005ffmt_005fformatDate_0026_005fvalue_005ftype_005fpattern_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fjamwiki_005feditComment_0026_005ftopic_005fcomment_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvalue_005fescape;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fjamwiki_005flinkParam_0026_005fvalue_005fkey_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fchoose;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvirtualWiki_005fvalue_005ftext_005fstyle_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fjamwiki_005fimage_0026_005fvirtualWiki_005fvalue_005fmaxWidth_005fmaxHeight_005fallowEnlarge_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fescapeXml_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fforEach_0026_005fvar_005fitems;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005ffmt_005fparam_0026_005fvalue_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fotherwise;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvirtualWiki_005fvalue_005ftext_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvirtualWiki_005fvalue;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvalue_005ftext_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fset_0026_005fvar_005fvalue_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fforEach_0026_005fvarStatus_005fvar_005fitems;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvalue_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fjamwiki_005fimage_0026_005fvalue_005fstyle_005fmaxWidth_005fmaxHeight_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fforEach_0026_005fend_005fbegin;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fjamwiki_005fpagination_0026_005ftotal_005frootUrl_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvalue;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.AnnotationProcessor _jsp_annotationprocessor;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _005fjspx_005ftagPool_005ffmt_005fsetBundle_0026_005fbasename_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005ffmt_005fparam = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005ffmt_005fformatDate_0026_005fvalue_005ftype_005fpattern_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fjamwiki_005feditComment_0026_005ftopic_005fcomment_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvalue_005fescape = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fjamwiki_005flinkParam_0026_005fvalue_005fkey_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fchoose = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvirtualWiki_005fvalue_005ftext_005fstyle_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fjamwiki_005fimage_0026_005fvirtualWiki_005fvalue_005fmaxWidth_005fmaxHeight_005fallowEnlarge_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fescapeXml_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fforEach_0026_005fvar_005fitems = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005ffmt_005fparam_0026_005fvalue_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fotherwise = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvirtualWiki_005fvalue_005ftext_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvirtualWiki_005fvalue = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvalue_005ftext_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fset_0026_005fvar_005fvalue_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fforEach_0026_005fvarStatus_005fvar_005fitems = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvalue_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fjamwiki_005fimage_0026_005fvalue_005fstyle_005fmaxWidth_005fmaxHeight_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fforEach_0026_005fend_005fbegin = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fjamwiki_005fpagination_0026_005ftotal_005frootUrl_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvalue = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_annotationprocessor = (org.apache.AnnotationProcessor) getServletConfig().getServletContext().getAttribute(org.apache.AnnotationProcessor.class.getName());
  }

  public void _jspDestroy() {
    _005fjspx_005ftagPool_005ffmt_005fsetBundle_0026_005fbasename_005fnobody.release();
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.release();
    _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey.release();
    _005fjspx_005ftagPool_005ffmt_005fparam.release();
    _005fjspx_005ftagPool_005ffmt_005fformatDate_0026_005fvalue_005ftype_005fpattern_005fnobody.release();
    _005fjspx_005ftagPool_005fjamwiki_005feditComment_0026_005ftopic_005fcomment_005fnobody.release();
    _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvalue_005fescape.release();
    _005fjspx_005ftagPool_005fjamwiki_005flinkParam_0026_005fvalue_005fkey_005fnobody.release();
    _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.release();
    _005fjspx_005ftagPool_005fc_005fchoose.release();
    _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.release();
    _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvirtualWiki_005fvalue_005ftext_005fstyle_005fnobody.release();
    _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.release();
    _005fjspx_005ftagPool_005fjamwiki_005fimage_0026_005fvirtualWiki_005fvalue_005fmaxWidth_005fmaxHeight_005fallowEnlarge_005fnobody.release();
    _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fescapeXml_005fnobody.release();
    _005fjspx_005ftagPool_005fc_005fforEach_0026_005fvar_005fitems.release();
    _005fjspx_005ftagPool_005ffmt_005fparam_0026_005fvalue_005fnobody.release();
    _005fjspx_005ftagPool_005fc_005fotherwise.release();
    _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvirtualWiki_005fvalue_005ftext_005fnobody.release();
    _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvirtualWiki_005fvalue.release();
    _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvalue_005ftext_005fnobody.release();
    _005fjspx_005ftagPool_005fc_005fset_0026_005fvar_005fvalue_005fnobody.release();
    _005fjspx_005ftagPool_005fc_005fforEach_0026_005fvarStatus_005fvar_005fitems.release();
    _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvalue_005fnobody.release();
    _005fjspx_005ftagPool_005fjamwiki_005fimage_0026_005fvalue_005fstyle_005fmaxWidth_005fmaxHeight_005fnobody.release();
    _005fjspx_005ftagPool_005fc_005fforEach_0026_005fend_005fbegin.release();
    _005fjspx_005ftagPool_005fjamwiki_005fpagination_0026_005ftotal_005frootUrl_005fnobody.release();
    _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvalue.release();
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html; charset=utf-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			"/WEB-INF/jsp/error.jsp", true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");

// no-cache headers
response.setHeader("Cache-Control", "private, s-maxage=0, max-age=0, must-revalidate");
response.setHeader("Expires", "Thu, 01 Jan 1970 00:00:00 GMT");
response.setHeader("Pragma", "no-cache");

      out.write('\r');
      out.write('\n');
      if (_jspx_meth_fmt_005fsetBundle_005f0(_jspx_page_context))
        return;
      out.write('\r');
      out.write('\n');

response.setContentType("text/html");
response.setCharacterEncoding("UTF-8");

      out.write('\r');
      out.write('\n');
      out.write("\r\n");
      out.write("\r\n");
      out.write('\r');
      out.write('\n');
      if (_jspx_meth_c_005fif_005f0(_jspx_page_context))
        return;
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      if (_jspx_meth_c_005fchoose_005f0(_jspx_page_context))
        return;
      out.write('\r');
      out.write('\n');
      out.write('\r');
      out.write('\n');
      out.write("\r\n");
      out.write("\r\n");
      if (_jspx_meth_c_005fif_005f11(_jspx_page_context))
        return;
      out.write('\r');
      out.write('\n');
      out.write('\r');
      out.write('\n');
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try { out.clearBuffer(); } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }

  private boolean _jspx_meth_fmt_005fsetBundle_005f0(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:setBundle
    org.apache.taglibs.standard.tag.rt.fmt.SetBundleTag _jspx_th_fmt_005fsetBundle_005f0 = (org.apache.taglibs.standard.tag.rt.fmt.SetBundleTag) _005fjspx_005ftagPool_005ffmt_005fsetBundle_0026_005fbasename_005fnobody.get(org.apache.taglibs.standard.tag.rt.fmt.SetBundleTag.class);
    _jspx_th_fmt_005fsetBundle_005f0.setPageContext(_jspx_page_context);
    _jspx_th_fmt_005fsetBundle_005f0.setParent(null);
    // /WEB-INF/jsp/page-init.jsp(32,0) name = basename type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_fmt_005fsetBundle_005f0.setBasename("ApplicationResources");
    int _jspx_eval_fmt_005fsetBundle_005f0 = _jspx_th_fmt_005fsetBundle_005f0.doStartTag();
    if (_jspx_th_fmt_005fsetBundle_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005ffmt_005fsetBundle_0026_005fbasename_005fnobody.reuse(_jspx_th_fmt_005fsetBundle_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005ffmt_005fsetBundle_0026_005fbasename_005fnobody.reuse(_jspx_th_fmt_005fsetBundle_005f0);
    return false;
  }

  private boolean _jspx_meth_c_005fif_005f0(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:if
    org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f0 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
    _jspx_th_c_005fif_005f0.setPageContext(_jspx_page_context);
    _jspx_th_c_005fif_005f0.setParent(null);
    // /WEB-INF/jsp/topic.jsp(26,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fif_005f0.setTest(((java.lang.Boolean) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${!empty version}", java.lang.Boolean.class, (PageContext)_jspx_page_context, null, false)).booleanValue());
    int _jspx_eval_c_005fif_005f0 = _jspx_th_c_005fif_005f0.doStartTag();
    if (_jspx_eval_c_005fif_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("\r\n");
        out.write("\t<div id=\"contentSub\">\r\n");
        out.write("\t\t<div id=\"revision\">\r\n");
        out.write("\t\t\t");
        if (_jspx_meth_fmt_005fmessage_005f0(_jspx_th_c_005fif_005f0, _jspx_page_context))
          return true;
        out.write("\r\n");
        out.write("\t\t\t");
        if (_jspx_meth_c_005fif_005f1(_jspx_th_c_005fif_005f0, _jspx_page_context))
          return true;
        out.write("\r\n");
        out.write("\t\t\t");
        if (_jspx_meth_c_005fif_005f2(_jspx_th_c_005fif_005f0, _jspx_page_context))
          return true;
        out.write("\r\n");
        out.write("\t\t</div>\r\n");
        out.write("\t\t<div id=\"revision_nav\">\r\n");
        out.write("\t\t\t");
        if (_jspx_meth_c_005fif_005f3(_jspx_th_c_005fif_005f0, _jspx_page_context))
          return true;
        out.write("\r\n");
        out.write("\t\t\t");
        if (_jspx_meth_jamwiki_005flink_005f2(_jspx_th_c_005fif_005f0, _jspx_page_context))
          return true;
        out.write("\r\n");
        out.write("\t\t\t");
        if (_jspx_meth_c_005fif_005f4(_jspx_th_c_005fif_005f0, _jspx_page_context))
          return true;
        out.write("\r\n");
        out.write("\t\t</div>\r\n");
        out.write("\t</div>\r\n");
        int evalDoAfterBody = _jspx_th_c_005fif_005f0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_c_005fif_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f0);
    return false;
  }

  private boolean _jspx_meth_fmt_005fmessage_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_005fmessage_005f0 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_005fmessage_005f0.setPageContext(_jspx_page_context);
    _jspx_th_fmt_005fmessage_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f0);
    // /WEB-INF/jsp/topic.jsp(29,3) name = key type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_fmt_005fmessage_005f0.setKey("history.revision");
    int _jspx_eval_fmt_005fmessage_005f0 = _jspx_th_fmt_005fmessage_005f0.doStartTag();
    if (_jspx_eval_fmt_005fmessage_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_fmt_005fmessage_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_fmt_005fmessage_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_fmt_005fmessage_005f0.doInitBody();
      }
      do {
        out.write("\r\n");
        out.write("\t\t\t\t");
        if (_jspx_meth_fmt_005fparam_005f0(_jspx_th_fmt_005fmessage_005f0, _jspx_page_context))
          return true;
        out.write("\r\n");
        out.write("\t\t\t\t");
        if (_jspx_meth_fmt_005fparam_005f1(_jspx_th_fmt_005fmessage_005f0, _jspx_page_context))
          return true;
        out.write("\r\n");
        out.write("\t\t\t");
        int evalDoAfterBody = _jspx_th_fmt_005fmessage_005f0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_fmt_005fmessage_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.popBody();
      }
    }
    if (_jspx_th_fmt_005fmessage_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey.reuse(_jspx_th_fmt_005fmessage_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey.reuse(_jspx_th_fmt_005fmessage_005f0);
    return false;
  }

  private boolean _jspx_meth_fmt_005fparam_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_fmt_005fmessage_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:param
    org.apache.taglibs.standard.tag.rt.fmt.ParamTag _jspx_th_fmt_005fparam_005f0 = (org.apache.taglibs.standard.tag.rt.fmt.ParamTag) _005fjspx_005ftagPool_005ffmt_005fparam.get(org.apache.taglibs.standard.tag.rt.fmt.ParamTag.class);
    _jspx_th_fmt_005fparam_005f0.setPageContext(_jspx_page_context);
    _jspx_th_fmt_005fparam_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_fmt_005fmessage_005f0);
    int _jspx_eval_fmt_005fparam_005f0 = _jspx_th_fmt_005fparam_005f0.doStartTag();
    if (_jspx_eval_fmt_005fparam_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_fmt_005fparam_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_fmt_005fparam_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_fmt_005fparam_005f0.doInitBody();
      }
      do {
        if (_jspx_meth_fmt_005fformatDate_005f0(_jspx_th_fmt_005fparam_005f0, _jspx_page_context))
          return true;
        int evalDoAfterBody = _jspx_th_fmt_005fparam_005f0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_fmt_005fparam_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.popBody();
      }
    }
    if (_jspx_th_fmt_005fparam_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005ffmt_005fparam.reuse(_jspx_th_fmt_005fparam_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005ffmt_005fparam.reuse(_jspx_th_fmt_005fparam_005f0);
    return false;
  }

  private boolean _jspx_meth_fmt_005fformatDate_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_fmt_005fparam_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:formatDate
    org.apache.taglibs.standard.tag.rt.fmt.FormatDateTag _jspx_th_fmt_005fformatDate_005f0 = (org.apache.taglibs.standard.tag.rt.fmt.FormatDateTag) _005fjspx_005ftagPool_005ffmt_005fformatDate_0026_005fvalue_005ftype_005fpattern_005fnobody.get(org.apache.taglibs.standard.tag.rt.fmt.FormatDateTag.class);
    _jspx_th_fmt_005fformatDate_005f0.setPageContext(_jspx_page_context);
    _jspx_th_fmt_005fformatDate_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_fmt_005fparam_005f0);
    // /WEB-INF/jsp/topic.jsp(30,15) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_fmt_005fformatDate_005f0.setValue((java.util.Date) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${version.changeDate}", java.util.Date.class, (PageContext)_jspx_page_context, null, false));
    // /WEB-INF/jsp/topic.jsp(30,15) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_fmt_005fformatDate_005f0.setType("both");
    // /WEB-INF/jsp/topic.jsp(30,15) name = pattern type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_fmt_005fformatDate_005f0.setPattern((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageInfo.datePatternDateAndTime}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_fmt_005fformatDate_005f0 = _jspx_th_fmt_005fformatDate_005f0.doStartTag();
    if (_jspx_th_fmt_005fformatDate_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005ffmt_005fformatDate_0026_005fvalue_005ftype_005fpattern_005fnobody.reuse(_jspx_th_fmt_005fformatDate_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005ffmt_005fformatDate_0026_005fvalue_005ftype_005fpattern_005fnobody.reuse(_jspx_th_fmt_005fformatDate_005f0);
    return false;
  }

  private boolean _jspx_meth_fmt_005fparam_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_fmt_005fmessage_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:param
    org.apache.taglibs.standard.tag.rt.fmt.ParamTag _jspx_th_fmt_005fparam_005f1 = (org.apache.taglibs.standard.tag.rt.fmt.ParamTag) _005fjspx_005ftagPool_005ffmt_005fparam.get(org.apache.taglibs.standard.tag.rt.fmt.ParamTag.class);
    _jspx_th_fmt_005fparam_005f1.setPageContext(_jspx_page_context);
    _jspx_th_fmt_005fparam_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_fmt_005fmessage_005f0);
    int _jspx_eval_fmt_005fparam_005f1 = _jspx_th_fmt_005fparam_005f1.doStartTag();
    if (_jspx_eval_fmt_005fparam_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_fmt_005fparam_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_fmt_005fparam_005f1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_fmt_005fparam_005f1.doInitBody();
      }
      do {
        if (_jspx_meth_jamwiki_005ft_005fuserLinks_005f0(_jspx_th_fmt_005fparam_005f1, _jspx_page_context))
          return true;
        int evalDoAfterBody = _jspx_th_fmt_005fparam_005f1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_fmt_005fparam_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.popBody();
      }
    }
    if (_jspx_th_fmt_005fparam_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005ffmt_005fparam.reuse(_jspx_th_fmt_005fparam_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005ffmt_005fparam.reuse(_jspx_th_fmt_005fparam_005f1);
    return false;
  }

  private boolean _jspx_meth_jamwiki_005ft_005fuserLinks_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_fmt_005fparam_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  jamwiki_t:userLinks
    org.apache.jsp.tag.web.userLinks_tag _jspx_th_jamwiki_005ft_005fuserLinks_005f0 = new org.apache.jsp.tag.web.userLinks_tag();
    org.apache.jasper.runtime.AnnotationHelper.postConstruct(_jsp_annotationprocessor, _jspx_th_jamwiki_005ft_005fuserLinks_005f0);
    _jspx_th_jamwiki_005ft_005fuserLinks_005f0.setJspContext(_jspx_page_context);
    _jspx_th_jamwiki_005ft_005fuserLinks_005f0.setParent(_jspx_th_fmt_005fparam_005f1);
    // /WEB-INF/jsp/topic.jsp(31,15) name = pageInfo type = org.jamwiki.servlets.WikiPageInfo reqTime = true required = true fragment = false deferredValue = false expectedTypeName = java.lang.String deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005ft_005fuserLinks_005f0.setPageInfo((org.jamwiki.servlets.WikiPageInfo) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageInfo}", org.jamwiki.servlets.WikiPageInfo.class, (PageContext)_jspx_page_context, null, false));
    // /WEB-INF/jsp/topic.jsp(31,15) name = userDisplay type = java.lang.String reqTime = true required = true fragment = false deferredValue = false expectedTypeName = java.lang.String deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005ft_005fuserLinks_005f0.setUserDisplay((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${version.authorName}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
    _jspx_th_jamwiki_005ft_005fuserLinks_005f0.doTag();
    org.apache.jasper.runtime.AnnotationHelper.preDestroy(_jsp_annotationprocessor, _jspx_th_jamwiki_005ft_005fuserLinks_005f0);
    return false;
  }

  private boolean _jspx_meth_c_005fif_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:if
    org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f1 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
    _jspx_th_c_005fif_005f1.setPageContext(_jspx_page_context);
    _jspx_th_c_005fif_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f0);
    // /WEB-INF/jsp/topic.jsp(33,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fif_005f1.setTest(((java.lang.Boolean) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${!empty version.changeComment}", java.lang.Boolean.class, (PageContext)_jspx_page_context, null, false)).booleanValue());
    int _jspx_eval_c_005fif_005f1 = _jspx_th_c_005fif_005f1.doStartTag();
    if (_jspx_eval_c_005fif_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("|&#160;(<span class=\"edit-comment\">");
        if (_jspx_meth_jamwiki_005feditComment_005f0(_jspx_th_c_005fif_005f1, _jspx_page_context))
          return true;
        out.write("</span>)");
        int evalDoAfterBody = _jspx_th_c_005fif_005f1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_c_005fif_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f1);
    return false;
  }

  private boolean _jspx_meth_jamwiki_005feditComment_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  jamwiki:editComment
    org.jamwiki.taglib.EditCommentTag _jspx_th_jamwiki_005feditComment_005f0 = (org.jamwiki.taglib.EditCommentTag) _005fjspx_005ftagPool_005fjamwiki_005feditComment_0026_005ftopic_005fcomment_005fnobody.get(org.jamwiki.taglib.EditCommentTag.class);
    _jspx_th_jamwiki_005feditComment_005f0.setPageContext(_jspx_page_context);
    _jspx_th_jamwiki_005feditComment_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f1);
    // /WEB-INF/jsp/topic.jsp(33,83) name = comment type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005feditComment_005f0.setComment((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${version.changeComment}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
    // /WEB-INF/jsp/topic.jsp(33,83) name = topic type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005feditComment_005f0.setTopic((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${version.topicName}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_jamwiki_005feditComment_005f0 = _jspx_th_jamwiki_005feditComment_005f0.doStartTag();
    if (_jspx_th_jamwiki_005feditComment_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fjamwiki_005feditComment_0026_005ftopic_005fcomment_005fnobody.reuse(_jspx_th_jamwiki_005feditComment_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fjamwiki_005feditComment_0026_005ftopic_005fcomment_005fnobody.reuse(_jspx_th_jamwiki_005feditComment_005f0);
    return false;
  }

  private boolean _jspx_meth_c_005fif_005f2(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:if
    org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f2 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
    _jspx_th_c_005fif_005f2.setPageContext(_jspx_page_context);
    _jspx_th_c_005fif_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f0);
    // /WEB-INF/jsp/topic.jsp(34,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fif_005f2.setTest(((java.lang.Boolean) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${!empty version.changeTypeNotification}", java.lang.Boolean.class, (PageContext)_jspx_page_context, null, false)).booleanValue());
    int _jspx_eval_c_005fif_005f2 = _jspx_th_c_005fif_005f2.doStartTag();
    if (_jspx_eval_c_005fif_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write('<');
        out.write('b');
        out.write('>');
        out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${version.changeTypeNotification}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
        out.write("</b>");
        int evalDoAfterBody = _jspx_th_c_005fif_005f2.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_c_005fif_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f2);
    return false;
  }

  private boolean _jspx_meth_c_005fif_005f3(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:if
    org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f3 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
    _jspx_th_c_005fif_005f3.setPageContext(_jspx_page_context);
    _jspx_th_c_005fif_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f0);
    // /WEB-INF/jsp/topic.jsp(37,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fif_005f3.setTest(((java.lang.Boolean) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${!empty version.previousTopicVersionId}", java.lang.Boolean.class, (PageContext)_jspx_page_context, null, false)).booleanValue());
    int _jspx_eval_c_005fif_005f3 = _jspx_th_c_005fif_005f3.doStartTag();
    if (_jspx_eval_c_005fif_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("\r\n");
        out.write("\t\t\t\t(");
        if (_jspx_meth_jamwiki_005flink_005f0(_jspx_th_c_005fif_005f3, _jspx_page_context))
          return true;
        out.write(")\r\n");
        out.write("\t\t\t\t");
        if (_jspx_meth_jamwiki_005flink_005f1(_jspx_th_c_005fif_005f3, _jspx_page_context))
          return true;
        out.write("&#160|\r\n");
        out.write("\t\t\t");
        int evalDoAfterBody = _jspx_th_c_005fif_005f3.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_c_005fif_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f3);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f3);
    return false;
  }

  private boolean _jspx_meth_jamwiki_005flink_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f3, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  jamwiki:link
    org.jamwiki.taglib.LinkTag _jspx_th_jamwiki_005flink_005f0 = (org.jamwiki.taglib.LinkTag) _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvalue_005fescape.get(org.jamwiki.taglib.LinkTag.class);
    _jspx_th_jamwiki_005flink_005f0.setPageContext(_jspx_page_context);
    _jspx_th_jamwiki_005flink_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f3);
    // /WEB-INF/jsp/topic.jsp(38,5) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005flink_005f0.setValue("Special:Diff");
    // /WEB-INF/jsp/topic.jsp(38,5) name = escape type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005flink_005f0.setEscape("false");
    int _jspx_eval_jamwiki_005flink_005f0 = _jspx_th_jamwiki_005flink_005f0.doStartTag();
    if (_jspx_eval_jamwiki_005flink_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_jamwiki_005flink_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_jamwiki_005flink_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_jamwiki_005flink_005f0.doInitBody();
      }
      do {
        if (_jspx_meth_jamwiki_005flinkParam_005f0(_jspx_th_jamwiki_005flink_005f0, _jspx_page_context))
          return true;
        if (_jspx_meth_jamwiki_005flinkParam_005f1(_jspx_th_jamwiki_005flink_005f0, _jspx_page_context))
          return true;
        if (_jspx_meth_jamwiki_005flinkParam_005f2(_jspx_th_jamwiki_005flink_005f0, _jspx_page_context))
          return true;
        if (_jspx_meth_fmt_005fmessage_005f1(_jspx_th_jamwiki_005flink_005f0, _jspx_page_context))
          return true;
        int evalDoAfterBody = _jspx_th_jamwiki_005flink_005f0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_jamwiki_005flink_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.popBody();
      }
    }
    if (_jspx_th_jamwiki_005flink_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvalue_005fescape.reuse(_jspx_th_jamwiki_005flink_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvalue_005fescape.reuse(_jspx_th_jamwiki_005flink_005f0);
    return false;
  }

  private boolean _jspx_meth_jamwiki_005flinkParam_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_jamwiki_005flink_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  jamwiki:linkParam
    org.jamwiki.taglib.LinkParamTag _jspx_th_jamwiki_005flinkParam_005f0 = (org.jamwiki.taglib.LinkParamTag) _005fjspx_005ftagPool_005fjamwiki_005flinkParam_0026_005fvalue_005fkey_005fnobody.get(org.jamwiki.taglib.LinkParamTag.class);
    _jspx_th_jamwiki_005flinkParam_005f0.setPageContext(_jspx_page_context);
    _jspx_th_jamwiki_005flinkParam_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_jamwiki_005flink_005f0);
    // /WEB-INF/jsp/topic.jsp(38,55) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005flinkParam_005f0.setKey("topic");
    // /WEB-INF/jsp/topic.jsp(38,55) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005flinkParam_005f0.setValue((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${version.topicName}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_jamwiki_005flinkParam_005f0 = _jspx_th_jamwiki_005flinkParam_005f0.doStartTag();
    if (_jspx_th_jamwiki_005flinkParam_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fjamwiki_005flinkParam_0026_005fvalue_005fkey_005fnobody.reuse(_jspx_th_jamwiki_005flinkParam_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fjamwiki_005flinkParam_0026_005fvalue_005fkey_005fnobody.reuse(_jspx_th_jamwiki_005flinkParam_005f0);
    return false;
  }

  private boolean _jspx_meth_jamwiki_005flinkParam_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_jamwiki_005flink_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  jamwiki:linkParam
    org.jamwiki.taglib.LinkParamTag _jspx_th_jamwiki_005flinkParam_005f1 = (org.jamwiki.taglib.LinkParamTag) _005fjspx_005ftagPool_005fjamwiki_005flinkParam_0026_005fvalue_005fkey_005fnobody.get(org.jamwiki.taglib.LinkParamTag.class);
    _jspx_th_jamwiki_005flinkParam_005f1.setPageContext(_jspx_page_context);
    _jspx_th_jamwiki_005flinkParam_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_jamwiki_005flink_005f0);
    // /WEB-INF/jsp/topic.jsp(38,117) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005flinkParam_005f1.setKey("version1");
    // /WEB-INF/jsp/topic.jsp(38,117) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005flinkParam_005f1.setValue((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${version.topicVersionId}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_jamwiki_005flinkParam_005f1 = _jspx_th_jamwiki_005flinkParam_005f1.doStartTag();
    if (_jspx_th_jamwiki_005flinkParam_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fjamwiki_005flinkParam_0026_005fvalue_005fkey_005fnobody.reuse(_jspx_th_jamwiki_005flinkParam_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fjamwiki_005flinkParam_0026_005fvalue_005fkey_005fnobody.reuse(_jspx_th_jamwiki_005flinkParam_005f1);
    return false;
  }

  private boolean _jspx_meth_jamwiki_005flinkParam_005f2(javax.servlet.jsp.tagext.JspTag _jspx_th_jamwiki_005flink_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  jamwiki:linkParam
    org.jamwiki.taglib.LinkParamTag _jspx_th_jamwiki_005flinkParam_005f2 = (org.jamwiki.taglib.LinkParamTag) _005fjspx_005ftagPool_005fjamwiki_005flinkParam_0026_005fvalue_005fkey_005fnobody.get(org.jamwiki.taglib.LinkParamTag.class);
    _jspx_th_jamwiki_005flinkParam_005f2.setPageContext(_jspx_page_context);
    _jspx_th_jamwiki_005flinkParam_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_jamwiki_005flink_005f0);
    // /WEB-INF/jsp/topic.jsp(38,187) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005flinkParam_005f2.setKey("version2");
    // /WEB-INF/jsp/topic.jsp(38,187) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005flinkParam_005f2.setValue((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${version.previousTopicVersionId}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_jamwiki_005flinkParam_005f2 = _jspx_th_jamwiki_005flinkParam_005f2.doStartTag();
    if (_jspx_th_jamwiki_005flinkParam_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fjamwiki_005flinkParam_0026_005fvalue_005fkey_005fnobody.reuse(_jspx_th_jamwiki_005flinkParam_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005fjamwiki_005flinkParam_0026_005fvalue_005fkey_005fnobody.reuse(_jspx_th_jamwiki_005flinkParam_005f2);
    return false;
  }

  private boolean _jspx_meth_fmt_005fmessage_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_jamwiki_005flink_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_005fmessage_005f1 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_005fmessage_005f1.setPageContext(_jspx_page_context);
    _jspx_th_fmt_005fmessage_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_jamwiki_005flink_005f0);
    // /WEB-INF/jsp/topic.jsp(38,265) name = key type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_fmt_005fmessage_005f1.setKey("common.caption.diff");
    int _jspx_eval_fmt_005fmessage_005f1 = _jspx_th_fmt_005fmessage_005f1.doStartTag();
    if (_jspx_th_fmt_005fmessage_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f1);
    return false;
  }

  private boolean _jspx_meth_jamwiki_005flink_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f3, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  jamwiki:link
    org.jamwiki.taglib.LinkTag _jspx_th_jamwiki_005flink_005f1 = (org.jamwiki.taglib.LinkTag) _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvalue_005fescape.get(org.jamwiki.taglib.LinkTag.class);
    _jspx_th_jamwiki_005flink_005f1.setPageContext(_jspx_page_context);
    _jspx_th_jamwiki_005flink_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f3);
    // /WEB-INF/jsp/topic.jsp(39,4) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005flink_005f1.setValue("Special:History");
    // /WEB-INF/jsp/topic.jsp(39,4) name = escape type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005flink_005f1.setEscape("false");
    int _jspx_eval_jamwiki_005flink_005f1 = _jspx_th_jamwiki_005flink_005f1.doStartTag();
    if (_jspx_eval_jamwiki_005flink_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_jamwiki_005flink_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_jamwiki_005flink_005f1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_jamwiki_005flink_005f1.doInitBody();
      }
      do {
        if (_jspx_meth_jamwiki_005flinkParam_005f3(_jspx_th_jamwiki_005flink_005f1, _jspx_page_context))
          return true;
        if (_jspx_meth_jamwiki_005flinkParam_005f4(_jspx_th_jamwiki_005flink_005f1, _jspx_page_context))
          return true;
        if (_jspx_meth_fmt_005fmessage_005f2(_jspx_th_jamwiki_005flink_005f1, _jspx_page_context))
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
      _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvalue_005fescape.reuse(_jspx_th_jamwiki_005flink_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvalue_005fescape.reuse(_jspx_th_jamwiki_005flink_005f1);
    return false;
  }

  private boolean _jspx_meth_jamwiki_005flinkParam_005f3(javax.servlet.jsp.tagext.JspTag _jspx_th_jamwiki_005flink_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  jamwiki:linkParam
    org.jamwiki.taglib.LinkParamTag _jspx_th_jamwiki_005flinkParam_005f3 = (org.jamwiki.taglib.LinkParamTag) _005fjspx_005ftagPool_005fjamwiki_005flinkParam_0026_005fvalue_005fkey_005fnobody.get(org.jamwiki.taglib.LinkParamTag.class);
    _jspx_th_jamwiki_005flinkParam_005f3.setPageContext(_jspx_page_context);
    _jspx_th_jamwiki_005flinkParam_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_jamwiki_005flink_005f1);
    // /WEB-INF/jsp/topic.jsp(39,57) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005flinkParam_005f3.setKey("topic");
    // /WEB-INF/jsp/topic.jsp(39,57) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005flinkParam_005f3.setValue((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${version.topicName}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_jamwiki_005flinkParam_005f3 = _jspx_th_jamwiki_005flinkParam_005f3.doStartTag();
    if (_jspx_th_jamwiki_005flinkParam_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fjamwiki_005flinkParam_0026_005fvalue_005fkey_005fnobody.reuse(_jspx_th_jamwiki_005flinkParam_005f3);
      return true;
    }
    _005fjspx_005ftagPool_005fjamwiki_005flinkParam_0026_005fvalue_005fkey_005fnobody.reuse(_jspx_th_jamwiki_005flinkParam_005f3);
    return false;
  }

  private boolean _jspx_meth_jamwiki_005flinkParam_005f4(javax.servlet.jsp.tagext.JspTag _jspx_th_jamwiki_005flink_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  jamwiki:linkParam
    org.jamwiki.taglib.LinkParamTag _jspx_th_jamwiki_005flinkParam_005f4 = (org.jamwiki.taglib.LinkParamTag) _005fjspx_005ftagPool_005fjamwiki_005flinkParam_0026_005fvalue_005fkey_005fnobody.get(org.jamwiki.taglib.LinkParamTag.class);
    _jspx_th_jamwiki_005flinkParam_005f4.setPageContext(_jspx_page_context);
    _jspx_th_jamwiki_005flinkParam_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_jamwiki_005flink_005f1);
    // /WEB-INF/jsp/topic.jsp(39,119) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005flinkParam_005f4.setKey("topicVersionId");
    // /WEB-INF/jsp/topic.jsp(39,119) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005flinkParam_005f4.setValue((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${version.previousTopicVersionId}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_jamwiki_005flinkParam_005f4 = _jspx_th_jamwiki_005flinkParam_005f4.doStartTag();
    if (_jspx_th_jamwiki_005flinkParam_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fjamwiki_005flinkParam_0026_005fvalue_005fkey_005fnobody.reuse(_jspx_th_jamwiki_005flinkParam_005f4);
      return true;
    }
    _005fjspx_005ftagPool_005fjamwiki_005flinkParam_0026_005fvalue_005fkey_005fnobody.reuse(_jspx_th_jamwiki_005flinkParam_005f4);
    return false;
  }

  private boolean _jspx_meth_fmt_005fmessage_005f2(javax.servlet.jsp.tagext.JspTag _jspx_th_jamwiki_005flink_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_005fmessage_005f2 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_005fmessage_005f2.setPageContext(_jspx_page_context);
    _jspx_th_fmt_005fmessage_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_jamwiki_005flink_005f1);
    // /WEB-INF/jsp/topic.jsp(39,203) name = key type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_fmt_005fmessage_005f2.setKey("history.revision.previous");
    int _jspx_eval_fmt_005fmessage_005f2 = _jspx_th_fmt_005fmessage_005f2.doStartTag();
    if (_jspx_th_fmt_005fmessage_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f2);
    return false;
  }

  private boolean _jspx_meth_jamwiki_005flink_005f2(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  jamwiki:link
    org.jamwiki.taglib.LinkTag _jspx_th_jamwiki_005flink_005f2 = (org.jamwiki.taglib.LinkTag) _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvalue_005fescape.get(org.jamwiki.taglib.LinkTag.class);
    _jspx_th_jamwiki_005flink_005f2.setPageContext(_jspx_page_context);
    _jspx_th_jamwiki_005flink_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f0);
    // /WEB-INF/jsp/topic.jsp(41,3) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005flink_005f2.setValue((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${version.topicName}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
    // /WEB-INF/jsp/topic.jsp(41,3) name = escape type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005flink_005f2.setEscape("false");
    int _jspx_eval_jamwiki_005flink_005f2 = _jspx_th_jamwiki_005flink_005f2.doStartTag();
    if (_jspx_eval_jamwiki_005flink_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_jamwiki_005flink_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_jamwiki_005flink_005f2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_jamwiki_005flink_005f2.doInitBody();
      }
      do {
        if (_jspx_meth_fmt_005fmessage_005f3(_jspx_th_jamwiki_005flink_005f2, _jspx_page_context))
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
      _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvalue_005fescape.reuse(_jspx_th_jamwiki_005flink_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvalue_005fescape.reuse(_jspx_th_jamwiki_005flink_005f2);
    return false;
  }

  private boolean _jspx_meth_fmt_005fmessage_005f3(javax.servlet.jsp.tagext.JspTag _jspx_th_jamwiki_005flink_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_005fmessage_005f3 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_005fmessage_005f3.setPageContext(_jspx_page_context);
    _jspx_th_fmt_005fmessage_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_jamwiki_005flink_005f2);
    // /WEB-INF/jsp/topic.jsp(41,61) name = key type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_fmt_005fmessage_005f3.setKey("history.revision.current");
    int _jspx_eval_fmt_005fmessage_005f3 = _jspx_th_fmt_005fmessage_005f3.doStartTag();
    if (_jspx_th_fmt_005fmessage_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f3);
      return true;
    }
    _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f3);
    return false;
  }

  private boolean _jspx_meth_c_005fif_005f4(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:if
    org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f4 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
    _jspx_th_c_005fif_005f4.setPageContext(_jspx_page_context);
    _jspx_th_c_005fif_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f0);
    // /WEB-INF/jsp/topic.jsp(42,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fif_005f4.setTest(((java.lang.Boolean) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${!empty nextTopicVersionId}", java.lang.Boolean.class, (PageContext)_jspx_page_context, null, false)).booleanValue());
    int _jspx_eval_c_005fif_005f4 = _jspx_th_c_005fif_005f4.doStartTag();
    if (_jspx_eval_c_005fif_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("\r\n");
        out.write("\t\t\t\t|&#160");
        if (_jspx_meth_jamwiki_005flink_005f3(_jspx_th_c_005fif_005f4, _jspx_page_context))
          return true;
        out.write("\r\n");
        out.write("\t\t\t\t(");
        if (_jspx_meth_jamwiki_005flink_005f4(_jspx_th_c_005fif_005f4, _jspx_page_context))
          return true;
        out.write(")\r\n");
        out.write("\t\t\t");
        int evalDoAfterBody = _jspx_th_c_005fif_005f4.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_c_005fif_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f4);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f4);
    return false;
  }

  private boolean _jspx_meth_jamwiki_005flink_005f3(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  jamwiki:link
    org.jamwiki.taglib.LinkTag _jspx_th_jamwiki_005flink_005f3 = (org.jamwiki.taglib.LinkTag) _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvalue_005fescape.get(org.jamwiki.taglib.LinkTag.class);
    _jspx_th_jamwiki_005flink_005f3.setPageContext(_jspx_page_context);
    _jspx_th_jamwiki_005flink_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f4);
    // /WEB-INF/jsp/topic.jsp(43,10) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005flink_005f3.setValue("Special:History");
    // /WEB-INF/jsp/topic.jsp(43,10) name = escape type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005flink_005f3.setEscape("false");
    int _jspx_eval_jamwiki_005flink_005f3 = _jspx_th_jamwiki_005flink_005f3.doStartTag();
    if (_jspx_eval_jamwiki_005flink_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_jamwiki_005flink_005f3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_jamwiki_005flink_005f3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_jamwiki_005flink_005f3.doInitBody();
      }
      do {
        if (_jspx_meth_jamwiki_005flinkParam_005f5(_jspx_th_jamwiki_005flink_005f3, _jspx_page_context))
          return true;
        if (_jspx_meth_jamwiki_005flinkParam_005f6(_jspx_th_jamwiki_005flink_005f3, _jspx_page_context))
          return true;
        if (_jspx_meth_fmt_005fmessage_005f4(_jspx_th_jamwiki_005flink_005f3, _jspx_page_context))
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
      _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvalue_005fescape.reuse(_jspx_th_jamwiki_005flink_005f3);
      return true;
    }
    _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvalue_005fescape.reuse(_jspx_th_jamwiki_005flink_005f3);
    return false;
  }

  private boolean _jspx_meth_jamwiki_005flinkParam_005f5(javax.servlet.jsp.tagext.JspTag _jspx_th_jamwiki_005flink_005f3, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  jamwiki:linkParam
    org.jamwiki.taglib.LinkParamTag _jspx_th_jamwiki_005flinkParam_005f5 = (org.jamwiki.taglib.LinkParamTag) _005fjspx_005ftagPool_005fjamwiki_005flinkParam_0026_005fvalue_005fkey_005fnobody.get(org.jamwiki.taglib.LinkParamTag.class);
    _jspx_th_jamwiki_005flinkParam_005f5.setPageContext(_jspx_page_context);
    _jspx_th_jamwiki_005flinkParam_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_jamwiki_005flink_005f3);
    // /WEB-INF/jsp/topic.jsp(43,63) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005flinkParam_005f5.setKey("topic");
    // /WEB-INF/jsp/topic.jsp(43,63) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005flinkParam_005f5.setValue((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${version.topicName}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_jamwiki_005flinkParam_005f5 = _jspx_th_jamwiki_005flinkParam_005f5.doStartTag();
    if (_jspx_th_jamwiki_005flinkParam_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fjamwiki_005flinkParam_0026_005fvalue_005fkey_005fnobody.reuse(_jspx_th_jamwiki_005flinkParam_005f5);
      return true;
    }
    _005fjspx_005ftagPool_005fjamwiki_005flinkParam_0026_005fvalue_005fkey_005fnobody.reuse(_jspx_th_jamwiki_005flinkParam_005f5);
    return false;
  }

  private boolean _jspx_meth_jamwiki_005flinkParam_005f6(javax.servlet.jsp.tagext.JspTag _jspx_th_jamwiki_005flink_005f3, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  jamwiki:linkParam
    org.jamwiki.taglib.LinkParamTag _jspx_th_jamwiki_005flinkParam_005f6 = (org.jamwiki.taglib.LinkParamTag) _005fjspx_005ftagPool_005fjamwiki_005flinkParam_0026_005fvalue_005fkey_005fnobody.get(org.jamwiki.taglib.LinkParamTag.class);
    _jspx_th_jamwiki_005flinkParam_005f6.setPageContext(_jspx_page_context);
    _jspx_th_jamwiki_005flinkParam_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_jamwiki_005flink_005f3);
    // /WEB-INF/jsp/topic.jsp(43,125) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005flinkParam_005f6.setKey("topicVersionId");
    // /WEB-INF/jsp/topic.jsp(43,125) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005flinkParam_005f6.setValue((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${nextTopicVersionId}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_jamwiki_005flinkParam_005f6 = _jspx_th_jamwiki_005flinkParam_005f6.doStartTag();
    if (_jspx_th_jamwiki_005flinkParam_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fjamwiki_005flinkParam_0026_005fvalue_005fkey_005fnobody.reuse(_jspx_th_jamwiki_005flinkParam_005f6);
      return true;
    }
    _005fjspx_005ftagPool_005fjamwiki_005flinkParam_0026_005fvalue_005fkey_005fnobody.reuse(_jspx_th_jamwiki_005flinkParam_005f6);
    return false;
  }

  private boolean _jspx_meth_fmt_005fmessage_005f4(javax.servlet.jsp.tagext.JspTag _jspx_th_jamwiki_005flink_005f3, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_005fmessage_005f4 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_005fmessage_005f4.setPageContext(_jspx_page_context);
    _jspx_th_fmt_005fmessage_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_jamwiki_005flink_005f3);
    // /WEB-INF/jsp/topic.jsp(43,197) name = key type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_fmt_005fmessage_005f4.setKey("history.revision.next");
    int _jspx_eval_fmt_005fmessage_005f4 = _jspx_th_fmt_005fmessage_005f4.doStartTag();
    if (_jspx_th_fmt_005fmessage_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f4);
      return true;
    }
    _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f4);
    return false;
  }

  private boolean _jspx_meth_jamwiki_005flink_005f4(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  jamwiki:link
    org.jamwiki.taglib.LinkTag _jspx_th_jamwiki_005flink_005f4 = (org.jamwiki.taglib.LinkTag) _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvalue_005fescape.get(org.jamwiki.taglib.LinkTag.class);
    _jspx_th_jamwiki_005flink_005f4.setPageContext(_jspx_page_context);
    _jspx_th_jamwiki_005flink_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f4);
    // /WEB-INF/jsp/topic.jsp(44,5) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005flink_005f4.setValue("Special:Diff");
    // /WEB-INF/jsp/topic.jsp(44,5) name = escape type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005flink_005f4.setEscape("false");
    int _jspx_eval_jamwiki_005flink_005f4 = _jspx_th_jamwiki_005flink_005f4.doStartTag();
    if (_jspx_eval_jamwiki_005flink_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_jamwiki_005flink_005f4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_jamwiki_005flink_005f4.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_jamwiki_005flink_005f4.doInitBody();
      }
      do {
        if (_jspx_meth_jamwiki_005flinkParam_005f7(_jspx_th_jamwiki_005flink_005f4, _jspx_page_context))
          return true;
        if (_jspx_meth_jamwiki_005flinkParam_005f8(_jspx_th_jamwiki_005flink_005f4, _jspx_page_context))
          return true;
        if (_jspx_meth_jamwiki_005flinkParam_005f9(_jspx_th_jamwiki_005flink_005f4, _jspx_page_context))
          return true;
        if (_jspx_meth_fmt_005fmessage_005f5(_jspx_th_jamwiki_005flink_005f4, _jspx_page_context))
          return true;
        int evalDoAfterBody = _jspx_th_jamwiki_005flink_005f4.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_jamwiki_005flink_005f4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.popBody();
      }
    }
    if (_jspx_th_jamwiki_005flink_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvalue_005fescape.reuse(_jspx_th_jamwiki_005flink_005f4);
      return true;
    }
    _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvalue_005fescape.reuse(_jspx_th_jamwiki_005flink_005f4);
    return false;
  }

  private boolean _jspx_meth_jamwiki_005flinkParam_005f7(javax.servlet.jsp.tagext.JspTag _jspx_th_jamwiki_005flink_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  jamwiki:linkParam
    org.jamwiki.taglib.LinkParamTag _jspx_th_jamwiki_005flinkParam_005f7 = (org.jamwiki.taglib.LinkParamTag) _005fjspx_005ftagPool_005fjamwiki_005flinkParam_0026_005fvalue_005fkey_005fnobody.get(org.jamwiki.taglib.LinkParamTag.class);
    _jspx_th_jamwiki_005flinkParam_005f7.setPageContext(_jspx_page_context);
    _jspx_th_jamwiki_005flinkParam_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_jamwiki_005flink_005f4);
    // /WEB-INF/jsp/topic.jsp(44,55) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005flinkParam_005f7.setKey("topic");
    // /WEB-INF/jsp/topic.jsp(44,55) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005flinkParam_005f7.setValue((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${version.topicName}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_jamwiki_005flinkParam_005f7 = _jspx_th_jamwiki_005flinkParam_005f7.doStartTag();
    if (_jspx_th_jamwiki_005flinkParam_005f7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fjamwiki_005flinkParam_0026_005fvalue_005fkey_005fnobody.reuse(_jspx_th_jamwiki_005flinkParam_005f7);
      return true;
    }
    _005fjspx_005ftagPool_005fjamwiki_005flinkParam_0026_005fvalue_005fkey_005fnobody.reuse(_jspx_th_jamwiki_005flinkParam_005f7);
    return false;
  }

  private boolean _jspx_meth_jamwiki_005flinkParam_005f8(javax.servlet.jsp.tagext.JspTag _jspx_th_jamwiki_005flink_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  jamwiki:linkParam
    org.jamwiki.taglib.LinkParamTag _jspx_th_jamwiki_005flinkParam_005f8 = (org.jamwiki.taglib.LinkParamTag) _005fjspx_005ftagPool_005fjamwiki_005flinkParam_0026_005fvalue_005fkey_005fnobody.get(org.jamwiki.taglib.LinkParamTag.class);
    _jspx_th_jamwiki_005flinkParam_005f8.setPageContext(_jspx_page_context);
    _jspx_th_jamwiki_005flinkParam_005f8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_jamwiki_005flink_005f4);
    // /WEB-INF/jsp/topic.jsp(44,117) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005flinkParam_005f8.setKey("version1");
    // /WEB-INF/jsp/topic.jsp(44,117) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005flinkParam_005f8.setValue((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${nextTopicVersionId}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_jamwiki_005flinkParam_005f8 = _jspx_th_jamwiki_005flinkParam_005f8.doStartTag();
    if (_jspx_th_jamwiki_005flinkParam_005f8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fjamwiki_005flinkParam_0026_005fvalue_005fkey_005fnobody.reuse(_jspx_th_jamwiki_005flinkParam_005f8);
      return true;
    }
    _005fjspx_005ftagPool_005fjamwiki_005flinkParam_0026_005fvalue_005fkey_005fnobody.reuse(_jspx_th_jamwiki_005flinkParam_005f8);
    return false;
  }

  private boolean _jspx_meth_jamwiki_005flinkParam_005f9(javax.servlet.jsp.tagext.JspTag _jspx_th_jamwiki_005flink_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  jamwiki:linkParam
    org.jamwiki.taglib.LinkParamTag _jspx_th_jamwiki_005flinkParam_005f9 = (org.jamwiki.taglib.LinkParamTag) _005fjspx_005ftagPool_005fjamwiki_005flinkParam_0026_005fvalue_005fkey_005fnobody.get(org.jamwiki.taglib.LinkParamTag.class);
    _jspx_th_jamwiki_005flinkParam_005f9.setPageContext(_jspx_page_context);
    _jspx_th_jamwiki_005flinkParam_005f9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_jamwiki_005flink_005f4);
    // /WEB-INF/jsp/topic.jsp(44,183) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005flinkParam_005f9.setKey("version2");
    // /WEB-INF/jsp/topic.jsp(44,183) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005flinkParam_005f9.setValue((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${version.topicVersionId}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_jamwiki_005flinkParam_005f9 = _jspx_th_jamwiki_005flinkParam_005f9.doStartTag();
    if (_jspx_th_jamwiki_005flinkParam_005f9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fjamwiki_005flinkParam_0026_005fvalue_005fkey_005fnobody.reuse(_jspx_th_jamwiki_005flinkParam_005f9);
      return true;
    }
    _005fjspx_005ftagPool_005fjamwiki_005flinkParam_0026_005fvalue_005fkey_005fnobody.reuse(_jspx_th_jamwiki_005flinkParam_005f9);
    return false;
  }

  private boolean _jspx_meth_fmt_005fmessage_005f5(javax.servlet.jsp.tagext.JspTag _jspx_th_jamwiki_005flink_005f4, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_005fmessage_005f5 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_005fmessage_005f5.setPageContext(_jspx_page_context);
    _jspx_th_fmt_005fmessage_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_jamwiki_005flink_005f4);
    // /WEB-INF/jsp/topic.jsp(44,253) name = key type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_fmt_005fmessage_005f5.setKey("common.caption.diff");
    int _jspx_eval_fmt_005fmessage_005f5 = _jspx_th_fmt_005fmessage_005f5.doStartTag();
    if (_jspx_th_fmt_005fmessage_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f5);
      return true;
    }
    _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f5);
    return false;
  }

  private boolean _jspx_meth_c_005fchoose_005f0(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:choose
    org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f0 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
    _jspx_th_c_005fchoose_005f0.setPageContext(_jspx_page_context);
    _jspx_th_c_005fchoose_005f0.setParent(null);
    int _jspx_eval_c_005fchoose_005f0 = _jspx_th_c_005fchoose_005f0.doStartTag();
    if (_jspx_eval_c_005fchoose_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write('\r');
        out.write('\n');
        out.write('	');
        if (_jspx_meth_c_005fwhen_005f0(_jspx_th_c_005fchoose_005f0, _jspx_page_context))
          return true;
        out.write('\r');
        out.write('\n');
        out.write('	');
        if (_jspx_meth_c_005fotherwise_005f1(_jspx_th_c_005fchoose_005f0, _jspx_page_context))
          return true;
        out.write('\r');
        out.write('\n');
        int evalDoAfterBody = _jspx_th_c_005fchoose_005f0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_c_005fchoose_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f0);
    return false;
  }

  private boolean _jspx_meth_c_005fwhen_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fchoose_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:when
    org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f0 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
    _jspx_th_c_005fwhen_005f0.setPageContext(_jspx_page_context);
    _jspx_th_c_005fwhen_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f0);
    // /WEB-INF/jsp/view-topic-include.jsp(21,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fwhen_005f0.setTest(((java.lang.Boolean) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${empty notopic}", java.lang.Boolean.class, (PageContext)_jspx_page_context, null, false)).booleanValue());
    int _jspx_eval_c_005fwhen_005f0 = _jspx_th_c_005fwhen_005f0.doStartTag();
    if (_jspx_eval_c_005fwhen_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("\r\n");
        out.write("\t\t");
        if (_jspx_meth_c_005fif_005f5(_jspx_th_c_005fwhen_005f0, _jspx_page_context))
          return true;
        out.write('\r');
        out.write('\n');
        out.write('	');
        int evalDoAfterBody = _jspx_th_c_005fwhen_005f0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_c_005fwhen_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f0);
    return false;
  }

  private boolean _jspx_meth_c_005fif_005f5(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:if
    org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f5 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
    _jspx_th_c_005fif_005f5.setPageContext(_jspx_page_context);
    _jspx_th_c_005fif_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f0);
    // /WEB-INF/jsp/view-topic-include.jsp(22,2) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fif_005f5.setTest(((java.lang.Boolean) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${!empty topicObject}", java.lang.Boolean.class, (PageContext)_jspx_page_context, null, false)).booleanValue());
    int _jspx_eval_c_005fif_005f5 = _jspx_th_c_005fif_005f5.doStartTag();
    if (_jspx_eval_c_005fif_005f5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("\r\n");
        out.write("\t\t\t<div id=\"content-article\">\r\n");
        out.write("\t\t\t");
        if (_jspx_meth_c_005fif_005f6(_jspx_th_c_005fif_005f5, _jspx_page_context))
          return true;
        out.write("\r\n");
        out.write("\t\t\t");
        if (_jspx_meth_c_005fif_005f7(_jspx_th_c_005fif_005f5, _jspx_page_context))
          return true;
        out.write("\r\n");
        out.write("\t\t\t");
        if (_jspx_meth_c_005fif_005f8(_jspx_th_c_005fif_005f5, _jspx_page_context))
          return true;
        out.write("\r\n");
        out.write("\t\t\t");
        if (_jspx_meth_c_005fout_005f3(_jspx_th_c_005fif_005f5, _jspx_page_context))
          return true;
        out.write("\r\n");
        out.write("\t\t\t</div>\r\n");
        out.write("\t\t\t<div class=\"clear\"></div>\r\n");
        out.write("\t\t\t");
        if (_jspx_meth_c_005fif_005f9(_jspx_th_c_005fif_005f5, _jspx_page_context))
          return true;
        out.write("\r\n");
        out.write("\t\t");
        int evalDoAfterBody = _jspx_th_c_005fif_005f5.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_c_005fif_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f5);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f5);
    return false;
  }

  private boolean _jspx_meth_c_005fif_005f6(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f5, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:if
    org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f6 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
    _jspx_th_c_005fif_005f6.setPageContext(_jspx_page_context);
    _jspx_th_c_005fif_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f5);
    // /WEB-INF/jsp/view-topic-include.jsp(24,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fif_005f6.setTest(((java.lang.Boolean) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${!empty sharedImageTopicObject}", java.lang.Boolean.class, (PageContext)_jspx_page_context, null, false)).booleanValue());
    int _jspx_eval_c_005fif_005f6 = _jspx_th_c_005fif_005f6.doStartTag();
    if (_jspx_eval_c_005fif_005f6 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("\r\n");
        out.write("\t\t\t\t<div id=\"shared-image-message\">");
        if (_jspx_meth_fmt_005fmessage_005f6(_jspx_th_c_005fif_005f6, _jspx_page_context))
          return true;
        out.write("</div>\r\n");
        out.write("\t\t\t");
        int evalDoAfterBody = _jspx_th_c_005fif_005f6.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_c_005fif_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f6);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f6);
    return false;
  }

  private boolean _jspx_meth_fmt_005fmessage_005f6(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f6, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_005fmessage_005f6 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_005fmessage_005f6.setPageContext(_jspx_page_context);
    _jspx_th_fmt_005fmessage_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f6);
    // /WEB-INF/jsp/view-topic-include.jsp(25,35) name = key type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_fmt_005fmessage_005f6.setKey("topic.sharedImage");
    int _jspx_eval_fmt_005fmessage_005f6 = _jspx_th_fmt_005fmessage_005f6.doStartTag();
    if (_jspx_eval_fmt_005fmessage_005f6 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_fmt_005fmessage_005f6 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_fmt_005fmessage_005f6.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_fmt_005fmessage_005f6.doInitBody();
      }
      do {
        if (_jspx_meth_fmt_005fparam_005f2(_jspx_th_fmt_005fmessage_005f6, _jspx_page_context))
          return true;
        int evalDoAfterBody = _jspx_th_fmt_005fmessage_005f6.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_fmt_005fmessage_005f6 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.popBody();
      }
    }
    if (_jspx_th_fmt_005fmessage_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey.reuse(_jspx_th_fmt_005fmessage_005f6);
      return true;
    }
    _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey.reuse(_jspx_th_fmt_005fmessage_005f6);
    return false;
  }

  private boolean _jspx_meth_fmt_005fparam_005f2(javax.servlet.jsp.tagext.JspTag _jspx_th_fmt_005fmessage_005f6, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:param
    org.apache.taglibs.standard.tag.rt.fmt.ParamTag _jspx_th_fmt_005fparam_005f2 = (org.apache.taglibs.standard.tag.rt.fmt.ParamTag) _005fjspx_005ftagPool_005ffmt_005fparam.get(org.apache.taglibs.standard.tag.rt.fmt.ParamTag.class);
    _jspx_th_fmt_005fparam_005f2.setPageContext(_jspx_page_context);
    _jspx_th_fmt_005fparam_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_fmt_005fmessage_005f6);
    int _jspx_eval_fmt_005fparam_005f2 = _jspx_th_fmt_005fparam_005f2.doStartTag();
    if (_jspx_eval_fmt_005fparam_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_fmt_005fparam_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_fmt_005fparam_005f2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_fmt_005fparam_005f2.doInitBody();
      }
      do {
        if (_jspx_meth_jamwiki_005flink_005f5(_jspx_th_fmt_005fparam_005f2, _jspx_page_context))
          return true;
        int evalDoAfterBody = _jspx_th_fmt_005fparam_005f2.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_fmt_005fparam_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.popBody();
      }
    }
    if (_jspx_th_fmt_005fparam_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005ffmt_005fparam.reuse(_jspx_th_fmt_005fparam_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005ffmt_005fparam.reuse(_jspx_th_fmt_005fparam_005f2);
    return false;
  }

  private boolean _jspx_meth_jamwiki_005flink_005f5(javax.servlet.jsp.tagext.JspTag _jspx_th_fmt_005fparam_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  jamwiki:link
    org.jamwiki.taglib.LinkTag _jspx_th_jamwiki_005flink_005f5 = (org.jamwiki.taglib.LinkTag) _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvirtualWiki_005fvalue_005ftext_005fstyle_005fnobody.get(org.jamwiki.taglib.LinkTag.class);
    _jspx_th_jamwiki_005flink_005f5.setPageContext(_jspx_page_context);
    _jspx_th_jamwiki_005flink_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_fmt_005fparam_005f2);
    // /WEB-INF/jsp/view-topic-include.jsp(25,83) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005flink_005f5.setValue((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${sharedImageTopicObject.name}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
    // /WEB-INF/jsp/view-topic-include.jsp(25,83) name = virtualWiki type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005flink_005f5.setVirtualWiki((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${sharedImageTopicObject.virtualWiki}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
    // /WEB-INF/jsp/view-topic-include.jsp(25,83) name = text type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005flink_005f5.setText((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${sharedImageTopicObject.name}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
    // /WEB-INF/jsp/view-topic-include.jsp(25,83) name = style type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005flink_005f5.setStyle("interwikilink");
    int _jspx_eval_jamwiki_005flink_005f5 = _jspx_th_jamwiki_005flink_005f5.doStartTag();
    if (_jspx_th_jamwiki_005flink_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvirtualWiki_005fvalue_005ftext_005fstyle_005fnobody.reuse(_jspx_th_jamwiki_005flink_005f5);
      return true;
    }
    _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvirtualWiki_005fvalue_005ftext_005fstyle_005fnobody.reuse(_jspx_th_jamwiki_005flink_005f5);
    return false;
  }

  private boolean _jspx_meth_c_005fif_005f7(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f5, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:if
    org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f7 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
    _jspx_th_c_005fif_005f7.setPageContext(_jspx_page_context);
    _jspx_th_c_005fif_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f5);
    // /WEB-INF/jsp/view-topic-include.jsp(27,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fif_005f7.setTest(((java.lang.Boolean) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${topicImage}", java.lang.Boolean.class, (PageContext)_jspx_page_context, null, false)).booleanValue());
    int _jspx_eval_c_005fif_005f7 = _jspx_th_c_005fif_005f7.doStartTag();
    if (_jspx_eval_c_005fif_005f7 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("<a href=\"");
        if (_jspx_meth_c_005fout_005f0(_jspx_th_c_005fif_005f7, _jspx_page_context))
          return true;
        out.write("\" class=\"wikiimg\">");
        if (_jspx_meth_jamwiki_005fimage_005f0(_jspx_th_c_005fif_005f7, _jspx_page_context))
          return true;
        out.write("</a>");
        int evalDoAfterBody = _jspx_th_c_005fif_005f7.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_c_005fif_005f7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f7);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f7);
    return false;
  }

  private boolean _jspx_meth_c_005fout_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f7, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:out
    org.apache.taglibs.standard.tag.rt.core.OutTag _jspx_th_c_005fout_005f0 = (org.apache.taglibs.standard.tag.rt.core.OutTag) _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.get(org.apache.taglibs.standard.tag.rt.core.OutTag.class);
    _jspx_th_c_005fout_005f0.setPageContext(_jspx_page_context);
    _jspx_th_c_005fout_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f7);
    // /WEB-INF/jsp/view-topic-include.jsp(27,39) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fout_005f0.setValue((java.lang.Object) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${fileVersions[0].url}", java.lang.Object.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_c_005fout_005f0 = _jspx_th_c_005fout_005f0.doStartTag();
    if (_jspx_th_c_005fout_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.reuse(_jspx_th_c_005fout_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.reuse(_jspx_th_c_005fout_005f0);
    return false;
  }

  private boolean _jspx_meth_jamwiki_005fimage_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f7, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  jamwiki:image
    org.jamwiki.taglib.ImageLinkTag _jspx_th_jamwiki_005fimage_005f0 = (org.jamwiki.taglib.ImageLinkTag) _005fjspx_005ftagPool_005fjamwiki_005fimage_0026_005fvirtualWiki_005fvalue_005fmaxWidth_005fmaxHeight_005fallowEnlarge_005fnobody.get(org.jamwiki.taglib.ImageLinkTag.class);
    _jspx_th_jamwiki_005fimage_005f0.setPageContext(_jspx_page_context);
    _jspx_th_jamwiki_005fimage_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f7);
    // /WEB-INF/jsp/view-topic-include.jsp(27,97) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005fimage_005f0.setValue((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${topicObject.name}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
    // /WEB-INF/jsp/view-topic-include.jsp(27,97) name = virtualWiki type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005fimage_005f0.setVirtualWiki((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${topicObject.virtualWiki}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
    // /WEB-INF/jsp/view-topic-include.jsp(27,97) name = maxWidth type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005fimage_005f0.setMaxWidth("800");
    // /WEB-INF/jsp/view-topic-include.jsp(27,97) name = maxHeight type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005fimage_005f0.setMaxHeight("600");
    // /WEB-INF/jsp/view-topic-include.jsp(27,97) name = allowEnlarge type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005fimage_005f0.setAllowEnlarge("false");
    int _jspx_eval_jamwiki_005fimage_005f0 = _jspx_th_jamwiki_005fimage_005f0.doStartTag();
    if (_jspx_th_jamwiki_005fimage_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fjamwiki_005fimage_0026_005fvirtualWiki_005fvalue_005fmaxWidth_005fmaxHeight_005fallowEnlarge_005fnobody.reuse(_jspx_th_jamwiki_005fimage_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fjamwiki_005fimage_0026_005fvirtualWiki_005fvalue_005fmaxWidth_005fmaxHeight_005fallowEnlarge_005fnobody.reuse(_jspx_th_jamwiki_005fimage_005f0);
    return false;
  }

  private boolean _jspx_meth_c_005fif_005f8(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f5, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:if
    org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f8 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
    _jspx_th_c_005fif_005f8.setPageContext(_jspx_page_context);
    _jspx_th_c_005fif_005f8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f5);
    // /WEB-INF/jsp/view-topic-include.jsp(28,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fif_005f8.setTest(((java.lang.Boolean) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${topicFile}", java.lang.Boolean.class, (PageContext)_jspx_page_context, null, false)).booleanValue());
    int _jspx_eval_c_005fif_005f8 = _jspx_th_c_005fif_005f8.doStartTag();
    if (_jspx_eval_c_005fif_005f8 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("<div id=\"topic-file-download\">");
        if (_jspx_meth_fmt_005fmessage_005f7(_jspx_th_c_005fif_005f8, _jspx_page_context))
          return true;
        out.write(":&#160;<a href=\"");
        if (_jspx_meth_c_005fout_005f1(_jspx_th_c_005fif_005f8, _jspx_page_context))
          return true;
        out.write('"');
        out.write('>');
        if (_jspx_meth_c_005fout_005f2(_jspx_th_c_005fif_005f8, _jspx_page_context))
          return true;
        out.write("</a></div>");
        int evalDoAfterBody = _jspx_th_c_005fif_005f8.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_c_005fif_005f8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f8);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f8);
    return false;
  }

  private boolean _jspx_meth_fmt_005fmessage_005f7(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f8, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_005fmessage_005f7 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_005fmessage_005f7.setPageContext(_jspx_page_context);
    _jspx_th_fmt_005fmessage_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f8);
    // /WEB-INF/jsp/view-topic-include.jsp(28,59) name = key type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_fmt_005fmessage_005f7.setKey("topic.file.download");
    int _jspx_eval_fmt_005fmessage_005f7 = _jspx_th_fmt_005fmessage_005f7.doStartTag();
    if (_jspx_th_fmt_005fmessage_005f7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f7);
      return true;
    }
    _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f7);
    return false;
  }

  private boolean _jspx_meth_c_005fout_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f8, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:out
    org.apache.taglibs.standard.tag.rt.core.OutTag _jspx_th_c_005fout_005f1 = (org.apache.taglibs.standard.tag.rt.core.OutTag) _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.get(org.apache.taglibs.standard.tag.rt.core.OutTag.class);
    _jspx_th_c_005fout_005f1.setPageContext(_jspx_page_context);
    _jspx_th_c_005fout_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f8);
    // /WEB-INF/jsp/view-topic-include.jsp(28,116) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fout_005f1.setValue((java.lang.Object) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${fileVersions[0].url}", java.lang.Object.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_c_005fout_005f1 = _jspx_th_c_005fout_005f1.doStartTag();
    if (_jspx_th_c_005fout_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.reuse(_jspx_th_c_005fout_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.reuse(_jspx_th_c_005fout_005f1);
    return false;
  }

  private boolean _jspx_meth_c_005fout_005f2(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f8, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:out
    org.apache.taglibs.standard.tag.rt.core.OutTag _jspx_th_c_005fout_005f2 = (org.apache.taglibs.standard.tag.rt.core.OutTag) _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.get(org.apache.taglibs.standard.tag.rt.core.OutTag.class);
    _jspx_th_c_005fout_005f2.setPageContext(_jspx_page_context);
    _jspx_th_c_005fout_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f8);
    // /WEB-INF/jsp/view-topic-include.jsp(28,158) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fout_005f2.setValue((java.lang.Object) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${topicObject.name}", java.lang.Object.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_c_005fout_005f2 = _jspx_th_c_005fout_005f2.doStartTag();
    if (_jspx_th_c_005fout_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.reuse(_jspx_th_c_005fout_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.reuse(_jspx_th_c_005fout_005f2);
    return false;
  }

  private boolean _jspx_meth_c_005fout_005f3(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f5, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:out
    org.apache.taglibs.standard.tag.rt.core.OutTag _jspx_th_c_005fout_005f3 = (org.apache.taglibs.standard.tag.rt.core.OutTag) _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fescapeXml_005fnobody.get(org.apache.taglibs.standard.tag.rt.core.OutTag.class);
    _jspx_th_c_005fout_005f3.setPageContext(_jspx_page_context);
    _jspx_th_c_005fout_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f5);
    // /WEB-INF/jsp/view-topic-include.jsp(29,3) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fout_005f3.setValue((java.lang.Object) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${topicObject.topicContent}", java.lang.Object.class, (PageContext)_jspx_page_context, null, false));
    // /WEB-INF/jsp/view-topic-include.jsp(29,3) name = escapeXml type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fout_005f3.setEscapeXml(false);
    int _jspx_eval_c_005fout_005f3 = _jspx_th_c_005fout_005f3.doStartTag();
    if (_jspx_th_c_005fout_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fescapeXml_005fnobody.reuse(_jspx_th_c_005fout_005f3);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fescapeXml_005fnobody.reuse(_jspx_th_c_005fout_005f3);
    return false;
  }

  private boolean _jspx_meth_c_005fif_005f9(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f5, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:if
    org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f9 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
    _jspx_th_c_005fif_005f9.setPageContext(_jspx_page_context);
    _jspx_th_c_005fif_005f9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f5);
    // /WEB-INF/jsp/view-topic-include.jsp(32,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fif_005f9.setTest(((java.lang.Boolean) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${!empty fileVersions}", java.lang.Boolean.class, (PageContext)_jspx_page_context, null, false)).booleanValue());
    int _jspx_eval_c_005fif_005f9 = _jspx_th_c_005fif_005f9.doStartTag();
    if (_jspx_eval_c_005fif_005f9 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("\r\n");
        out.write("\t\t\t\t");
        out.write("\n");
        out.write("\t\t\t\t<h2>");
        if (_jspx_meth_fmt_005fmessage_005f8(_jspx_th_c_005fif_005f9, _jspx_page_context))
          return true;
        out.write("</h2>\r\n");
        out.write("\t\t\t\t<p>");
        if (_jspx_meth_fmt_005fmessage_005f9(_jspx_th_c_005fif_005f9, _jspx_page_context))
          return true;
        out.write("</p>\r\n");
        out.write("\t\t\t\t<ul>\r\n");
        out.write("\t\t\t\t\t");
        if (_jspx_meth_c_005fforEach_005f0(_jspx_th_c_005fif_005f9, _jspx_page_context))
          return true;
        out.write("\r\n");
        out.write("\t\t\t\t</ul>\r\n");
        out.write("\t\t\t");
        int evalDoAfterBody = _jspx_th_c_005fif_005f9.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_c_005fif_005f9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f9);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f9);
    return false;
  }

  private boolean _jspx_meth_fmt_005fmessage_005f8(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f9, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_005fmessage_005f8 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_005fmessage_005f8.setPageContext(_jspx_page_context);
    _jspx_th_fmt_005fmessage_005f8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f9);
    // /WEB-INF/jsp/view-topic-include.jsp(34,8) name = key type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_fmt_005fmessage_005f8.setKey("topic.filehistory");
    int _jspx_eval_fmt_005fmessage_005f8 = _jspx_th_fmt_005fmessage_005f8.doStartTag();
    if (_jspx_th_fmt_005fmessage_005f8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f8);
      return true;
    }
    _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f8);
    return false;
  }

  private boolean _jspx_meth_fmt_005fmessage_005f9(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f9, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_005fmessage_005f9 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_005fmessage_005f9.setPageContext(_jspx_page_context);
    _jspx_th_fmt_005fmessage_005f9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f9);
    // /WEB-INF/jsp/view-topic-include.jsp(35,7) name = key type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_fmt_005fmessage_005f9.setKey("topic.filehistory.click");
    int _jspx_eval_fmt_005fmessage_005f9 = _jspx_th_fmt_005fmessage_005f9.doStartTag();
    if (_jspx_th_fmt_005fmessage_005f9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f9);
      return true;
    }
    _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f9);
    return false;
  }

  private boolean _jspx_meth_c_005fforEach_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f9, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:forEach
    org.apache.taglibs.standard.tag.rt.core.ForEachTag _jspx_th_c_005fforEach_005f0 = (org.apache.taglibs.standard.tag.rt.core.ForEachTag) _005fjspx_005ftagPool_005fc_005fforEach_0026_005fvar_005fitems.get(org.apache.taglibs.standard.tag.rt.core.ForEachTag.class);
    _jspx_th_c_005fforEach_005f0.setPageContext(_jspx_page_context);
    _jspx_th_c_005fforEach_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f9);
    // /WEB-INF/jsp/view-topic-include.jsp(37,5) name = items type = java.lang.Object reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fforEach_005f0.setItems((java.lang.Object) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${fileVersions}", java.lang.Object.class, (PageContext)_jspx_page_context, null, false));
    // /WEB-INF/jsp/view-topic-include.jsp(37,5) name = var type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fforEach_005f0.setVar("fileVersion");
    int[] _jspx_push_body_count_c_005fforEach_005f0 = new int[] { 0 };
    try {
      int _jspx_eval_c_005fforEach_005f0 = _jspx_th_c_005fforEach_005f0.doStartTag();
      if (_jspx_eval_c_005fforEach_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        do {
          out.write("\r\n");
          out.write("\t\t\t\t\t<li>\n");
          out.write("\t\t\t\t\t<a href=\"");
          if (_jspx_meth_c_005fout_005f4(_jspx_th_c_005fforEach_005f0, _jspx_page_context, _jspx_push_body_count_c_005fforEach_005f0))
            return true;
          out.write('"');
          out.write('>');
          if (_jspx_meth_fmt_005fformatDate_005f1(_jspx_th_c_005fforEach_005f0, _jspx_page_context, _jspx_push_body_count_c_005fforEach_005f0))
            return true;
          out.write("</a>\r\t\t\t\t\t&#160;(");
          if (_jspx_meth_fmt_005fmessage_005f10(_jspx_th_c_005fforEach_005f0, _jspx_page_context, _jspx_push_body_count_c_005fforEach_005f0))
            return true;
          out.write(")\r\n");
          out.write("\t\t\t\t\t&#160;.&#160;.&#160;\r\n");
          out.write("\t\t\t\t\t");
          if (_jspx_meth_c_005fchoose_005f1(_jspx_th_c_005fforEach_005f0, _jspx_page_context, _jspx_push_body_count_c_005fforEach_005f0))
            return true;
          out.write("\r\n");
          out.write("\t\t\t\t\t");
          if (_jspx_meth_c_005fif_005f10(_jspx_th_c_005fforEach_005f0, _jspx_page_context, _jspx_push_body_count_c_005fforEach_005f0))
            return true;
          out.write("\r\n");
          out.write("\t\t\t\t\t</li>\r\n");
          out.write("\t\t\t\t\t");
          int evalDoAfterBody = _jspx_th_c_005fforEach_005f0.doAfterBody();
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
      }
      if (_jspx_th_c_005fforEach_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        return true;
      }
    } catch (Throwable _jspx_exception) {
      while (_jspx_push_body_count_c_005fforEach_005f0[0]-- > 0)
        out = _jspx_page_context.popBody();
      _jspx_th_c_005fforEach_005f0.doCatch(_jspx_exception);
    } finally {
      _jspx_th_c_005fforEach_005f0.doFinally();
      _005fjspx_005ftagPool_005fc_005fforEach_0026_005fvar_005fitems.reuse(_jspx_th_c_005fforEach_005f0);
    }
    return false;
  }

  private boolean _jspx_meth_c_005fout_005f4(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fforEach_005f0, PageContext _jspx_page_context, int[] _jspx_push_body_count_c_005fforEach_005f0)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:out
    org.apache.taglibs.standard.tag.rt.core.OutTag _jspx_th_c_005fout_005f4 = (org.apache.taglibs.standard.tag.rt.core.OutTag) _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.get(org.apache.taglibs.standard.tag.rt.core.OutTag.class);
    _jspx_th_c_005fout_005f4.setPageContext(_jspx_page_context);
    _jspx_th_c_005fout_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fforEach_005f0);
    // /WEB-INF/jsp/view-topic-include.jsp(39,14) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fout_005f4.setValue((java.lang.Object) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${fileVersion.url}", java.lang.Object.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_c_005fout_005f4 = _jspx_th_c_005fout_005f4.doStartTag();
    if (_jspx_th_c_005fout_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.reuse(_jspx_th_c_005fout_005f4);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.reuse(_jspx_th_c_005fout_005f4);
    return false;
  }

  private boolean _jspx_meth_fmt_005fformatDate_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fforEach_005f0, PageContext _jspx_page_context, int[] _jspx_push_body_count_c_005fforEach_005f0)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:formatDate
    org.apache.taglibs.standard.tag.rt.fmt.FormatDateTag _jspx_th_fmt_005fformatDate_005f1 = (org.apache.taglibs.standard.tag.rt.fmt.FormatDateTag) _005fjspx_005ftagPool_005ffmt_005fformatDate_0026_005fvalue_005ftype_005fpattern_005fnobody.get(org.apache.taglibs.standard.tag.rt.fmt.FormatDateTag.class);
    _jspx_th_fmt_005fformatDate_005f1.setPageContext(_jspx_page_context);
    _jspx_th_fmt_005fformatDate_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fforEach_005f0);
    // /WEB-INF/jsp/view-topic-include.jsp(39,52) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_fmt_005fformatDate_005f1.setValue((java.util.Date) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${fileVersion.uploadDate}", java.util.Date.class, (PageContext)_jspx_page_context, null, false));
    // /WEB-INF/jsp/view-topic-include.jsp(39,52) name = type type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_fmt_005fformatDate_005f1.setType("both");
    // /WEB-INF/jsp/view-topic-include.jsp(39,52) name = pattern type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_fmt_005fformatDate_005f1.setPattern((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageInfo.datePatternDateAndTime}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_fmt_005fformatDate_005f1 = _jspx_th_fmt_005fformatDate_005f1.doStartTag();
    if (_jspx_th_fmt_005fformatDate_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005ffmt_005fformatDate_0026_005fvalue_005ftype_005fpattern_005fnobody.reuse(_jspx_th_fmt_005fformatDate_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005ffmt_005fformatDate_0026_005fvalue_005ftype_005fpattern_005fnobody.reuse(_jspx_th_fmt_005fformatDate_005f1);
    return false;
  }

  private boolean _jspx_meth_fmt_005fmessage_005f10(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fforEach_005f0, PageContext _jspx_page_context, int[] _jspx_push_body_count_c_005fforEach_005f0)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_005fmessage_005f10 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_005fmessage_005f10.setPageContext(_jspx_page_context);
    _jspx_th_fmt_005fmessage_005f10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fforEach_005f0);
    // /WEB-INF/jsp/view-topic-include.jsp(39,178) name = key type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_fmt_005fmessage_005f10.setKey("topic.filesize.bytes");
    int _jspx_eval_fmt_005fmessage_005f10 = _jspx_th_fmt_005fmessage_005f10.doStartTag();
    if (_jspx_eval_fmt_005fmessage_005f10 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_fmt_005fmessage_005f10 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_push_body_count_c_005fforEach_005f0[0]++;
        _jspx_th_fmt_005fmessage_005f10.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_fmt_005fmessage_005f10.doInitBody();
      }
      do {
        if (_jspx_meth_fmt_005fparam_005f3(_jspx_th_fmt_005fmessage_005f10, _jspx_page_context, _jspx_push_body_count_c_005fforEach_005f0))
          return true;
        int evalDoAfterBody = _jspx_th_fmt_005fmessage_005f10.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_fmt_005fmessage_005f10 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.popBody();
        _jspx_push_body_count_c_005fforEach_005f0[0]--;
      }
    }
    if (_jspx_th_fmt_005fmessage_005f10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey.reuse(_jspx_th_fmt_005fmessage_005f10);
      return true;
    }
    _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey.reuse(_jspx_th_fmt_005fmessage_005f10);
    return false;
  }

  private boolean _jspx_meth_fmt_005fparam_005f3(javax.servlet.jsp.tagext.JspTag _jspx_th_fmt_005fmessage_005f10, PageContext _jspx_page_context, int[] _jspx_push_body_count_c_005fforEach_005f0)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:param
    org.apache.taglibs.standard.tag.rt.fmt.ParamTag _jspx_th_fmt_005fparam_005f3 = (org.apache.taglibs.standard.tag.rt.fmt.ParamTag) _005fjspx_005ftagPool_005ffmt_005fparam_0026_005fvalue_005fnobody.get(org.apache.taglibs.standard.tag.rt.fmt.ParamTag.class);
    _jspx_th_fmt_005fparam_005f3.setPageContext(_jspx_page_context);
    _jspx_th_fmt_005fparam_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_fmt_005fmessage_005f10);
    // /WEB-INF/jsp/view-topic-include.jsp(39,218) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_fmt_005fparam_005f3.setValue((java.lang.Object) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${fileVersion.fileSize}", java.lang.Object.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_fmt_005fparam_005f3 = _jspx_th_fmt_005fparam_005f3.doStartTag();
    if (_jspx_th_fmt_005fparam_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005ffmt_005fparam_0026_005fvalue_005fnobody.reuse(_jspx_th_fmt_005fparam_005f3);
      return true;
    }
    _005fjspx_005ftagPool_005ffmt_005fparam_0026_005fvalue_005fnobody.reuse(_jspx_th_fmt_005fparam_005f3);
    return false;
  }

  private boolean _jspx_meth_c_005fchoose_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fforEach_005f0, PageContext _jspx_page_context, int[] _jspx_push_body_count_c_005fforEach_005f0)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:choose
    org.apache.taglibs.standard.tag.common.core.ChooseTag _jspx_th_c_005fchoose_005f1 = (org.apache.taglibs.standard.tag.common.core.ChooseTag) _005fjspx_005ftagPool_005fc_005fchoose.get(org.apache.taglibs.standard.tag.common.core.ChooseTag.class);
    _jspx_th_c_005fchoose_005f1.setPageContext(_jspx_page_context);
    _jspx_th_c_005fchoose_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fforEach_005f0);
    int _jspx_eval_c_005fchoose_005f1 = _jspx_th_c_005fchoose_005f1.doStartTag();
    if (_jspx_eval_c_005fchoose_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("\r\n");
        out.write("\t\t\t\t\t\t");
        if (_jspx_meth_c_005fwhen_005f1(_jspx_th_c_005fchoose_005f1, _jspx_page_context, _jspx_push_body_count_c_005fforEach_005f0))
          return true;
        out.write("\r\n");
        out.write("\t\t\t\t\t\t");
        if (_jspx_meth_c_005fotherwise_005f0(_jspx_th_c_005fchoose_005f1, _jspx_page_context, _jspx_push_body_count_c_005fforEach_005f0))
          return true;
        out.write("\r\n");
        out.write("\t\t\t\t\t");
        int evalDoAfterBody = _jspx_th_c_005fchoose_005f1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_c_005fchoose_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fchoose.reuse(_jspx_th_c_005fchoose_005f1);
    return false;
  }

  private boolean _jspx_meth_c_005fwhen_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fchoose_005f1, PageContext _jspx_page_context, int[] _jspx_push_body_count_c_005fforEach_005f0)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:when
    org.apache.taglibs.standard.tag.rt.core.WhenTag _jspx_th_c_005fwhen_005f1 = (org.apache.taglibs.standard.tag.rt.core.WhenTag) _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.WhenTag.class);
    _jspx_th_c_005fwhen_005f1.setPageContext(_jspx_page_context);
    _jspx_th_c_005fwhen_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f1);
    // /WEB-INF/jsp/view-topic-include.jsp(42,6) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fwhen_005f1.setTest(((java.lang.Boolean) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${!empty sharedImageTopicObject}", java.lang.Boolean.class, (PageContext)_jspx_page_context, null, false)).booleanValue());
    int _jspx_eval_c_005fwhen_005f1 = _jspx_th_c_005fwhen_005f1.doStartTag();
    if (_jspx_eval_c_005fwhen_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("\r\n");
        out.write("\t\t\t\t\t\t\t");
        if (_jspx_meth_jamwiki_005flink_005f6(_jspx_th_c_005fwhen_005f1, _jspx_page_context, _jspx_push_body_count_c_005fforEach_005f0))
          return true;
        out.write("\r\n");
        out.write("\t\t\t\t\t\t");
        int evalDoAfterBody = _jspx_th_c_005fwhen_005f1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_c_005fwhen_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fwhen_0026_005ftest.reuse(_jspx_th_c_005fwhen_005f1);
    return false;
  }

  private boolean _jspx_meth_jamwiki_005flink_005f6(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fwhen_005f1, PageContext _jspx_page_context, int[] _jspx_push_body_count_c_005fforEach_005f0)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  jamwiki:link
    org.jamwiki.taglib.LinkTag _jspx_th_jamwiki_005flink_005f6 = (org.jamwiki.taglib.LinkTag) _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvirtualWiki_005fvalue_005ftext_005fstyle_005fnobody.get(org.jamwiki.taglib.LinkTag.class);
    _jspx_th_jamwiki_005flink_005f6.setPageContext(_jspx_page_context);
    _jspx_th_jamwiki_005flink_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fwhen_005f1);
    // /WEB-INF/jsp/view-topic-include.jsp(43,7) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005flink_005f6.setValue((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageInfo.namespaces[sharedImageTopicObject.virtualWiki]['User']}:${fileVersion.authorDisplay}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
    // /WEB-INF/jsp/view-topic-include.jsp(43,7) name = virtualWiki type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005flink_005f6.setVirtualWiki((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${sharedImageTopicObject.virtualWiki}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
    // /WEB-INF/jsp/view-topic-include.jsp(43,7) name = text type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005flink_005f6.setText((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${fileVersion.authorDisplay}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
    // /WEB-INF/jsp/view-topic-include.jsp(43,7) name = style type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005flink_005f6.setStyle("interwikilink");
    int _jspx_eval_jamwiki_005flink_005f6 = _jspx_th_jamwiki_005flink_005f6.doStartTag();
    if (_jspx_th_jamwiki_005flink_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvirtualWiki_005fvalue_005ftext_005fstyle_005fnobody.reuse(_jspx_th_jamwiki_005flink_005f6);
      return true;
    }
    _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvirtualWiki_005fvalue_005ftext_005fstyle_005fnobody.reuse(_jspx_th_jamwiki_005flink_005f6);
    return false;
  }

  private boolean _jspx_meth_c_005fotherwise_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fchoose_005f1, PageContext _jspx_page_context, int[] _jspx_push_body_count_c_005fforEach_005f0)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:otherwise
    org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f0 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
    _jspx_th_c_005fotherwise_005f0.setPageContext(_jspx_page_context);
    _jspx_th_c_005fotherwise_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f1);
    int _jspx_eval_c_005fotherwise_005f0 = _jspx_th_c_005fotherwise_005f0.doStartTag();
    if (_jspx_eval_c_005fotherwise_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("\r\n");
        out.write("\t\t\t\t\t\t\t");
        if (_jspx_meth_jamwiki_005flink_005f7(_jspx_th_c_005fotherwise_005f0, _jspx_page_context, _jspx_push_body_count_c_005fforEach_005f0))
          return true;
        out.write("\r\n");
        out.write("\t\t\t\t\t\t\t(");
        if (_jspx_meth_jamwiki_005flink_005f8(_jspx_th_c_005fotherwise_005f0, _jspx_page_context, _jspx_push_body_count_c_005fforEach_005f0))
          return true;
        out.write("&#160;|&#160;");
        if (_jspx_meth_jamwiki_005flink_005f9(_jspx_th_c_005fotherwise_005f0, _jspx_page_context, _jspx_push_body_count_c_005fforEach_005f0))
          return true;
        out.write(")\r\n");
        out.write("\t\t\t\t\t\t");
        int evalDoAfterBody = _jspx_th_c_005fotherwise_005f0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_c_005fotherwise_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f0);
    return false;
  }

  private boolean _jspx_meth_jamwiki_005flink_005f7(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fotherwise_005f0, PageContext _jspx_page_context, int[] _jspx_push_body_count_c_005fforEach_005f0)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  jamwiki:link
    org.jamwiki.taglib.LinkTag _jspx_th_jamwiki_005flink_005f7 = (org.jamwiki.taglib.LinkTag) _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvirtualWiki_005fvalue_005ftext_005fnobody.get(org.jamwiki.taglib.LinkTag.class);
    _jspx_th_jamwiki_005flink_005f7.setPageContext(_jspx_page_context);
    _jspx_th_jamwiki_005flink_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f0);
    // /WEB-INF/jsp/view-topic-include.jsp(46,7) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005flink_005f7.setValue((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageInfo.namespaces[topicObject.virtualWiki]['User']}:${fileVersion.authorDisplay}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
    // /WEB-INF/jsp/view-topic-include.jsp(46,7) name = virtualWiki type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005flink_005f7.setVirtualWiki((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${topicObject.virtualWiki}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
    // /WEB-INF/jsp/view-topic-include.jsp(46,7) name = text type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005flink_005f7.setText((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${fileVersion.authorDisplay}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_jamwiki_005flink_005f7 = _jspx_th_jamwiki_005flink_005f7.doStartTag();
    if (_jspx_th_jamwiki_005flink_005f7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvirtualWiki_005fvalue_005ftext_005fnobody.reuse(_jspx_th_jamwiki_005flink_005f7);
      return true;
    }
    _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvirtualWiki_005fvalue_005ftext_005fnobody.reuse(_jspx_th_jamwiki_005flink_005f7);
    return false;
  }

  private boolean _jspx_meth_jamwiki_005flink_005f8(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fotherwise_005f0, PageContext _jspx_page_context, int[] _jspx_push_body_count_c_005fforEach_005f0)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  jamwiki:link
    org.jamwiki.taglib.LinkTag _jspx_th_jamwiki_005flink_005f8 = (org.jamwiki.taglib.LinkTag) _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvirtualWiki_005fvalue.get(org.jamwiki.taglib.LinkTag.class);
    _jspx_th_jamwiki_005flink_005f8.setPageContext(_jspx_page_context);
    _jspx_th_jamwiki_005flink_005f8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f0);
    // /WEB-INF/jsp/view-topic-include.jsp(47,8) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005flink_005f8.setValue((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageInfo.namespaces[topicObject.virtualWiki]['User comments']}:${fileVersion.authorDisplay}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
    // /WEB-INF/jsp/view-topic-include.jsp(47,8) name = virtualWiki type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005flink_005f8.setVirtualWiki((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${topicObject.virtualWiki}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_jamwiki_005flink_005f8 = _jspx_th_jamwiki_005flink_005f8.doStartTag();
    if (_jspx_eval_jamwiki_005flink_005f8 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_jamwiki_005flink_005f8 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_push_body_count_c_005fforEach_005f0[0]++;
        _jspx_th_jamwiki_005flink_005f8.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_jamwiki_005flink_005f8.doInitBody();
      }
      do {
        if (_jspx_meth_fmt_005fmessage_005f11(_jspx_th_jamwiki_005flink_005f8, _jspx_page_context, _jspx_push_body_count_c_005fforEach_005f0))
          return true;
        int evalDoAfterBody = _jspx_th_jamwiki_005flink_005f8.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_jamwiki_005flink_005f8 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.popBody();
        _jspx_push_body_count_c_005fforEach_005f0[0]--;
      }
    }
    if (_jspx_th_jamwiki_005flink_005f8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvirtualWiki_005fvalue.reuse(_jspx_th_jamwiki_005flink_005f8);
      return true;
    }
    _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvirtualWiki_005fvalue.reuse(_jspx_th_jamwiki_005flink_005f8);
    return false;
  }

  private boolean _jspx_meth_fmt_005fmessage_005f11(javax.servlet.jsp.tagext.JspTag _jspx_th_jamwiki_005flink_005f8, PageContext _jspx_page_context, int[] _jspx_push_body_count_c_005fforEach_005f0)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_005fmessage_005f11 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_005fmessage_005f11.setPageContext(_jspx_page_context);
    _jspx_th_fmt_005fmessage_005f11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_jamwiki_005flink_005f8);
    // /WEB-INF/jsp/view-topic-include.jsp(47,165) name = key type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_fmt_005fmessage_005f11.setKey("recentchanges.caption.comments");
    int _jspx_eval_fmt_005fmessage_005f11 = _jspx_th_fmt_005fmessage_005f11.doStartTag();
    if (_jspx_th_fmt_005fmessage_005f11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f11);
      return true;
    }
    _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f11);
    return false;
  }

  private boolean _jspx_meth_jamwiki_005flink_005f9(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fotherwise_005f0, PageContext _jspx_page_context, int[] _jspx_push_body_count_c_005fforEach_005f0)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  jamwiki:link
    org.jamwiki.taglib.LinkTag _jspx_th_jamwiki_005flink_005f9 = (org.jamwiki.taglib.LinkTag) _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvirtualWiki_005fvalue.get(org.jamwiki.taglib.LinkTag.class);
    _jspx_th_jamwiki_005flink_005f9.setPageContext(_jspx_page_context);
    _jspx_th_jamwiki_005flink_005f9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f0);
    // /WEB-INF/jsp/view-topic-include.jsp(47,245) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005flink_005f9.setValue("Special:Contributions");
    // /WEB-INF/jsp/view-topic-include.jsp(47,245) name = virtualWiki type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005flink_005f9.setVirtualWiki((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${topicObject.virtualWiki}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_jamwiki_005flink_005f9 = _jspx_th_jamwiki_005flink_005f9.doStartTag();
    if (_jspx_eval_jamwiki_005flink_005f9 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_jamwiki_005flink_005f9 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_push_body_count_c_005fforEach_005f0[0]++;
        _jspx_th_jamwiki_005flink_005f9.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_jamwiki_005flink_005f9.doInitBody();
      }
      do {
        if (_jspx_meth_jamwiki_005flinkParam_005f10(_jspx_th_jamwiki_005flink_005f9, _jspx_page_context, _jspx_push_body_count_c_005fforEach_005f0))
          return true;
        if (_jspx_meth_fmt_005fmessage_005f12(_jspx_th_jamwiki_005flink_005f9, _jspx_page_context, _jspx_push_body_count_c_005fforEach_005f0))
          return true;
        int evalDoAfterBody = _jspx_th_jamwiki_005flink_005f9.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_jamwiki_005flink_005f9 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.popBody();
        _jspx_push_body_count_c_005fforEach_005f0[0]--;
      }
    }
    if (_jspx_th_jamwiki_005flink_005f9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvirtualWiki_005fvalue.reuse(_jspx_th_jamwiki_005flink_005f9);
      return true;
    }
    _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvirtualWiki_005fvalue.reuse(_jspx_th_jamwiki_005flink_005f9);
    return false;
  }

  private boolean _jspx_meth_jamwiki_005flinkParam_005f10(javax.servlet.jsp.tagext.JspTag _jspx_th_jamwiki_005flink_005f9, PageContext _jspx_page_context, int[] _jspx_push_body_count_c_005fforEach_005f0)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  jamwiki:linkParam
    org.jamwiki.taglib.LinkParamTag _jspx_th_jamwiki_005flinkParam_005f10 = (org.jamwiki.taglib.LinkParamTag) _005fjspx_005ftagPool_005fjamwiki_005flinkParam_0026_005fvalue_005fkey_005fnobody.get(org.jamwiki.taglib.LinkParamTag.class);
    _jspx_th_jamwiki_005flinkParam_005f10.setPageContext(_jspx_page_context);
    _jspx_th_jamwiki_005flinkParam_005f10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_jamwiki_005flink_005f9);
    // /WEB-INF/jsp/view-topic-include.jsp(47,330) name = key type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005flinkParam_005f10.setKey("contributor");
    // /WEB-INF/jsp/view-topic-include.jsp(47,330) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005flinkParam_005f10.setValue((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${fileVersion.authorDisplay}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_jamwiki_005flinkParam_005f10 = _jspx_th_jamwiki_005flinkParam_005f10.doStartTag();
    if (_jspx_th_jamwiki_005flinkParam_005f10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fjamwiki_005flinkParam_0026_005fvalue_005fkey_005fnobody.reuse(_jspx_th_jamwiki_005flinkParam_005f10);
      return true;
    }
    _005fjspx_005ftagPool_005fjamwiki_005flinkParam_0026_005fvalue_005fkey_005fnobody.reuse(_jspx_th_jamwiki_005flinkParam_005f10);
    return false;
  }

  private boolean _jspx_meth_fmt_005fmessage_005f12(javax.servlet.jsp.tagext.JspTag _jspx_th_jamwiki_005flink_005f9, PageContext _jspx_page_context, int[] _jspx_push_body_count_c_005fforEach_005f0)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_005fmessage_005f12 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_005fmessage_005f12.setPageContext(_jspx_page_context);
    _jspx_th_fmt_005fmessage_005f12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_jamwiki_005flink_005f9);
    // /WEB-INF/jsp/view-topic-include.jsp(47,406) name = key type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_fmt_005fmessage_005f12.setKey("recentchanges.caption.contributions");
    int _jspx_eval_fmt_005fmessage_005f12 = _jspx_th_fmt_005fmessage_005f12.doStartTag();
    if (_jspx_th_fmt_005fmessage_005f12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f12);
      return true;
    }
    _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f12);
    return false;
  }

  private boolean _jspx_meth_c_005fif_005f10(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fforEach_005f0, PageContext _jspx_page_context, int[] _jspx_push_body_count_c_005fforEach_005f0)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:if
    org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f10 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
    _jspx_th_c_005fif_005f10.setPageContext(_jspx_page_context);
    _jspx_th_c_005fif_005f10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fforEach_005f0);
    // /WEB-INF/jsp/view-topic-include.jsp(50,5) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fif_005f10.setTest(((java.lang.Boolean) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${!empty fileVersion.uploadComment}", java.lang.Boolean.class, (PageContext)_jspx_page_context, null, false)).booleanValue());
    int _jspx_eval_c_005fif_005f10 = _jspx_th_c_005fif_005f10.doStartTag();
    if (_jspx_eval_c_005fif_005f10 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("&#160;(<i>");
        if (_jspx_meth_c_005fout_005f5(_jspx_th_c_005fif_005f10, _jspx_page_context, _jspx_push_body_count_c_005fforEach_005f0))
          return true;
        out.write("</i>)");
        int evalDoAfterBody = _jspx_th_c_005fif_005f10.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_c_005fif_005f10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f10);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f10);
    return false;
  }

  private boolean _jspx_meth_c_005fout_005f5(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f10, PageContext _jspx_page_context, int[] _jspx_push_body_count_c_005fforEach_005f0)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:out
    org.apache.taglibs.standard.tag.rt.core.OutTag _jspx_th_c_005fout_005f5 = (org.apache.taglibs.standard.tag.rt.core.OutTag) _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.get(org.apache.taglibs.standard.tag.rt.core.OutTag.class);
    _jspx_th_c_005fout_005f5.setPageContext(_jspx_page_context);
    _jspx_th_c_005fout_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f10);
    // /WEB-INF/jsp/view-topic-include.jsp(50,64) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fout_005f5.setValue((java.lang.Object) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${fileVersion.uploadComment}", java.lang.Object.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_c_005fout_005f5 = _jspx_th_c_005fout_005f5.doStartTag();
    if (_jspx_th_c_005fout_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.reuse(_jspx_th_c_005fout_005f5);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fout_0026_005fvalue_005fnobody.reuse(_jspx_th_c_005fout_005f5);
    return false;
  }

  private boolean _jspx_meth_c_005fotherwise_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fchoose_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:otherwise
    org.apache.taglibs.standard.tag.common.core.OtherwiseTag _jspx_th_c_005fotherwise_005f1 = (org.apache.taglibs.standard.tag.common.core.OtherwiseTag) _005fjspx_005ftagPool_005fc_005fotherwise.get(org.apache.taglibs.standard.tag.common.core.OtherwiseTag.class);
    _jspx_th_c_005fotherwise_005f1.setPageContext(_jspx_page_context);
    _jspx_th_c_005fotherwise_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fchoose_005f0);
    int _jspx_eval_c_005fotherwise_005f1 = _jspx_th_c_005fotherwise_005f1.doStartTag();
    if (_jspx_eval_c_005fotherwise_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("\r\n");
        out.write("\t\t<div class=\"message\">");
        if (_jspx_meth_fmt_005fmessage_005f13(_jspx_th_c_005fotherwise_005f1, _jspx_page_context))
          return true;
        out.write("</div>\r\n");
        out.write("\t");
        int evalDoAfterBody = _jspx_th_c_005fotherwise_005f1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_c_005fotherwise_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fotherwise.reuse(_jspx_th_c_005fotherwise_005f1);
    return false;
  }

  private boolean _jspx_meth_fmt_005fmessage_005f13(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fotherwise_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_005fmessage_005f13 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_005fmessage_005f13.setPageContext(_jspx_page_context);
    _jspx_th_fmt_005fmessage_005f13.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fotherwise_005f1);
    // /WEB-INF/jsp/view-topic-include.jsp(58,23) name = key type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_fmt_005fmessage_005f13.setKey((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${notopic.key}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_fmt_005fmessage_005f13 = _jspx_th_fmt_005fmessage_005f13.doStartTag();
    if (_jspx_eval_fmt_005fmessage_005f13 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_fmt_005fmessage_005f13 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_fmt_005fmessage_005f13.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_fmt_005fmessage_005f13.doInitBody();
      }
      do {
        if (_jspx_meth_fmt_005fparam_005f4(_jspx_th_fmt_005fmessage_005f13, _jspx_page_context))
          return true;
        if (_jspx_meth_fmt_005fparam_005f5(_jspx_th_fmt_005fmessage_005f13, _jspx_page_context))
          return true;
        int evalDoAfterBody = _jspx_th_fmt_005fmessage_005f13.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_fmt_005fmessage_005f13 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.popBody();
      }
    }
    if (_jspx_th_fmt_005fmessage_005f13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey.reuse(_jspx_th_fmt_005fmessage_005f13);
      return true;
    }
    _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey.reuse(_jspx_th_fmt_005fmessage_005f13);
    return false;
  }

  private boolean _jspx_meth_fmt_005fparam_005f4(javax.servlet.jsp.tagext.JspTag _jspx_th_fmt_005fmessage_005f13, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:param
    org.apache.taglibs.standard.tag.rt.fmt.ParamTag _jspx_th_fmt_005fparam_005f4 = (org.apache.taglibs.standard.tag.rt.fmt.ParamTag) _005fjspx_005ftagPool_005ffmt_005fparam_0026_005fvalue_005fnobody.get(org.apache.taglibs.standard.tag.rt.fmt.ParamTag.class);
    _jspx_th_fmt_005fparam_005f4.setPageContext(_jspx_page_context);
    _jspx_th_fmt_005fparam_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_fmt_005fmessage_005f13);
    // /WEB-INF/jsp/view-topic-include.jsp(58,57) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_fmt_005fparam_005f4.setValue((java.lang.Object) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${notopic.params[0]}", java.lang.Object.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_fmt_005fparam_005f4 = _jspx_th_fmt_005fparam_005f4.doStartTag();
    if (_jspx_th_fmt_005fparam_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005ffmt_005fparam_0026_005fvalue_005fnobody.reuse(_jspx_th_fmt_005fparam_005f4);
      return true;
    }
    _005fjspx_005ftagPool_005ffmt_005fparam_0026_005fvalue_005fnobody.reuse(_jspx_th_fmt_005fparam_005f4);
    return false;
  }

  private boolean _jspx_meth_fmt_005fparam_005f5(javax.servlet.jsp.tagext.JspTag _jspx_th_fmt_005fmessage_005f13, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:param
    org.apache.taglibs.standard.tag.rt.fmt.ParamTag _jspx_th_fmt_005fparam_005f5 = (org.apache.taglibs.standard.tag.rt.fmt.ParamTag) _005fjspx_005ftagPool_005ffmt_005fparam.get(org.apache.taglibs.standard.tag.rt.fmt.ParamTag.class);
    _jspx_th_fmt_005fparam_005f5.setPageContext(_jspx_page_context);
    _jspx_th_fmt_005fparam_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_fmt_005fmessage_005f13);
    int _jspx_eval_fmt_005fparam_005f5 = _jspx_th_fmt_005fparam_005f5.doStartTag();
    if (_jspx_eval_fmt_005fparam_005f5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_fmt_005fparam_005f5 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_fmt_005fparam_005f5.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_fmt_005fparam_005f5.doInitBody();
      }
      do {
        if (_jspx_meth_jamwiki_005flink_005f10(_jspx_th_fmt_005fparam_005f5, _jspx_page_context))
          return true;
        int evalDoAfterBody = _jspx_th_fmt_005fparam_005f5.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_fmt_005fparam_005f5 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.popBody();
      }
    }
    if (_jspx_th_fmt_005fparam_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005ffmt_005fparam.reuse(_jspx_th_fmt_005fparam_005f5);
      return true;
    }
    _005fjspx_005ftagPool_005ffmt_005fparam.reuse(_jspx_th_fmt_005fparam_005f5);
    return false;
  }

  private boolean _jspx_meth_jamwiki_005flink_005f10(javax.servlet.jsp.tagext.JspTag _jspx_th_fmt_005fparam_005f5, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  jamwiki:link
    org.jamwiki.taglib.LinkTag _jspx_th_jamwiki_005flink_005f10 = (org.jamwiki.taglib.LinkTag) _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvalue_005ftext_005fnobody.get(org.jamwiki.taglib.LinkTag.class);
    _jspx_th_jamwiki_005flink_005f10.setPageContext(_jspx_page_context);
    _jspx_th_jamwiki_005flink_005f10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_fmt_005fparam_005f5);
    // /WEB-INF/jsp/view-topic-include.jsp(58,110) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005flink_005f10.setValue((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${notopic.params[0]}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
    // /WEB-INF/jsp/view-topic-include.jsp(58,110) name = text type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005flink_005f10.setText((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${notopic.params[0]}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_jamwiki_005flink_005f10 = _jspx_th_jamwiki_005flink_005f10.doStartTag();
    if (_jspx_th_jamwiki_005flink_005f10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvalue_005ftext_005fnobody.reuse(_jspx_th_jamwiki_005flink_005f10);
      return true;
    }
    _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvalue_005ftext_005fnobody.reuse(_jspx_th_jamwiki_005flink_005f10);
    return false;
  }

  private boolean _jspx_meth_c_005fif_005f11(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:if
    org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f11 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
    _jspx_th_c_005fif_005f11.setPageContext(_jspx_page_context);
    _jspx_th_c_005fif_005f11.setParent(null);
    // /WEB-INF/jsp/view-category-include.jsp(20,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fif_005f11.setTest(((java.lang.Boolean) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${empty notopic}", java.lang.Boolean.class, (PageContext)_jspx_page_context, null, false)).booleanValue());
    int _jspx_eval_c_005fif_005f11 = _jspx_th_c_005fif_005f11.doStartTag();
    if (_jspx_eval_c_005fif_005f11 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write('\r');
        out.write('\n');
        out.write('	');
        out.write("\r\n");
        out.write("\r\n");
        if (_jspx_meth_c_005fif_005f12(_jspx_th_c_005fif_005f11, _jspx_page_context))
          return true;
        out.write('\r');
        out.write('\n');
        out.write('	');
        if (_jspx_meth_c_005fif_005f20(_jspx_th_c_005fif_005f11, _jspx_page_context))
          return true;
        out.write('\r');
        out.write('\n');
        int evalDoAfterBody = _jspx_th_c_005fif_005f11.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_c_005fif_005f11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f11);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f11);
    return false;
  }

  private boolean _jspx_meth_c_005fif_005f12(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f11, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:if
    org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f12 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
    _jspx_th_c_005fif_005f12.setPageContext(_jspx_page_context);
    _jspx_th_c_005fif_005f12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f11);
    // /WEB-INF/jsp/category-include.jsp(20,0) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fif_005f12.setTest(((java.lang.Boolean) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${!empty categoryName}", java.lang.Boolean.class, (PageContext)_jspx_page_context, null, false)).booleanValue());
    int _jspx_eval_c_005fif_005f12 = _jspx_th_c_005fif_005f12.doStartTag();
    if (_jspx_eval_c_005fif_005f12 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write('\r');
        out.write('\n');
        out.write('	');
        if (_jspx_meth_c_005fif_005f13(_jspx_th_c_005fif_005f12, _jspx_page_context))
          return true;
        out.write("\r\n");
        out.write("\r\n");
        out.write("\t");
        if (_jspx_meth_c_005fif_005f15(_jspx_th_c_005fif_005f12, _jspx_page_context))
          return true;
        out.write("\r\n");
        out.write("\r\n");
        out.write("<h3>");
        if (_jspx_meth_fmt_005fmessage_005f18(_jspx_th_c_005fif_005f12, _jspx_page_context))
          return true;
        out.write("</h3>\r\n");
        out.write("\r\n");
        out.write("<div class=\"message\">");
        if (_jspx_meth_fmt_005fmessage_005f19(_jspx_th_c_005fif_005f12, _jspx_page_context))
          return true;
        out.write("</div>\r\n");
        out.write("\t");
        if (_jspx_meth_c_005fif_005f18(_jspx_th_c_005fif_005f12, _jspx_page_context))
          return true;
        out.write('\r');
        out.write('\n');
        int evalDoAfterBody = _jspx_th_c_005fif_005f12.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_c_005fif_005f12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f12);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f12);
    return false;
  }

  private boolean _jspx_meth_c_005fif_005f13(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f12, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:if
    org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f13 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
    _jspx_th_c_005fif_005f13.setPageContext(_jspx_page_context);
    _jspx_th_c_005fif_005f13.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f12);
    // /WEB-INF/jsp/category-include.jsp(21,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fif_005f13.setTest(((java.lang.Boolean) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${numSubCategories > 0}", java.lang.Boolean.class, (PageContext)_jspx_page_context, null, false)).booleanValue());
    int _jspx_eval_c_005fif_005f13 = _jspx_th_c_005fif_005f13.doStartTag();
    if (_jspx_eval_c_005fif_005f13 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("\r\n");
        out.write("<h3>");
        if (_jspx_meth_fmt_005fmessage_005f14(_jspx_th_c_005fif_005f13, _jspx_page_context))
          return true;
        out.write("</h3>\r\n");
        out.write("<div class=\"message\">");
        if (_jspx_meth_fmt_005fmessage_005f15(_jspx_th_c_005fif_005f13, _jspx_page_context))
          return true;
        out.write("</div>\r\n");
        out.write("\r\n");
        out.write("<table class=\"categories\"><tr><td>\r\n");
        out.write("<ul>\r\n");
        out.write("\t\t");
        if (_jspx_meth_c_005fset_005f0(_jspx_th_c_005fif_005f13, _jspx_page_context))
          return true;
        out.write("\r\n");
        out.write("\t\t");
        if (_jspx_meth_c_005fforEach_005f1(_jspx_th_c_005fif_005f13, _jspx_page_context))
          return true;
        out.write("\r\n");
        out.write("</ul>\r\n");
        out.write("\r\n");
        out.write("</td></tr></table>\r\n");
        out.write("\t");
        int evalDoAfterBody = _jspx_th_c_005fif_005f13.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_c_005fif_005f13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f13);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f13);
    return false;
  }

  private boolean _jspx_meth_fmt_005fmessage_005f14(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f13, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_005fmessage_005f14 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_005fmessage_005f14.setPageContext(_jspx_page_context);
    _jspx_th_fmt_005fmessage_005f14.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f13);
    // /WEB-INF/jsp/category-include.jsp(22,4) name = key type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_fmt_005fmessage_005f14.setKey("topic.category.subcategories");
    int _jspx_eval_fmt_005fmessage_005f14 = _jspx_th_fmt_005fmessage_005f14.doStartTag();
    if (_jspx_eval_fmt_005fmessage_005f14 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_fmt_005fmessage_005f14 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_fmt_005fmessage_005f14.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_fmt_005fmessage_005f14.doInitBody();
      }
      do {
        if (_jspx_meth_fmt_005fparam_005f6(_jspx_th_fmt_005fmessage_005f14, _jspx_page_context))
          return true;
        int evalDoAfterBody = _jspx_th_fmt_005fmessage_005f14.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_fmt_005fmessage_005f14 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.popBody();
      }
    }
    if (_jspx_th_fmt_005fmessage_005f14.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey.reuse(_jspx_th_fmt_005fmessage_005f14);
      return true;
    }
    _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey.reuse(_jspx_th_fmt_005fmessage_005f14);
    return false;
  }

  private boolean _jspx_meth_fmt_005fparam_005f6(javax.servlet.jsp.tagext.JspTag _jspx_th_fmt_005fmessage_005f14, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:param
    org.apache.taglibs.standard.tag.rt.fmt.ParamTag _jspx_th_fmt_005fparam_005f6 = (org.apache.taglibs.standard.tag.rt.fmt.ParamTag) _005fjspx_005ftagPool_005ffmt_005fparam_0026_005fvalue_005fnobody.get(org.apache.taglibs.standard.tag.rt.fmt.ParamTag.class);
    _jspx_th_fmt_005fparam_005f6.setPageContext(_jspx_page_context);
    _jspx_th_fmt_005fparam_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_fmt_005fmessage_005f14);
    // /WEB-INF/jsp/category-include.jsp(22,52) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_fmt_005fparam_005f6.setValue((java.lang.Object) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${categoryName}", java.lang.Object.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_fmt_005fparam_005f6 = _jspx_th_fmt_005fparam_005f6.doStartTag();
    if (_jspx_th_fmt_005fparam_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005ffmt_005fparam_0026_005fvalue_005fnobody.reuse(_jspx_th_fmt_005fparam_005f6);
      return true;
    }
    _005fjspx_005ftagPool_005ffmt_005fparam_0026_005fvalue_005fnobody.reuse(_jspx_th_fmt_005fparam_005f6);
    return false;
  }

  private boolean _jspx_meth_fmt_005fmessage_005f15(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f13, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_005fmessage_005f15 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_005fmessage_005f15.setPageContext(_jspx_page_context);
    _jspx_th_fmt_005fmessage_005f15.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f13);
    // /WEB-INF/jsp/category-include.jsp(23,21) name = key type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_fmt_005fmessage_005f15.setKey("topic.category.numsubcategories");
    int _jspx_eval_fmt_005fmessage_005f15 = _jspx_th_fmt_005fmessage_005f15.doStartTag();
    if (_jspx_eval_fmt_005fmessage_005f15 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_fmt_005fmessage_005f15 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_fmt_005fmessage_005f15.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_fmt_005fmessage_005f15.doInitBody();
      }
      do {
        if (_jspx_meth_fmt_005fparam_005f7(_jspx_th_fmt_005fmessage_005f15, _jspx_page_context))
          return true;
        if (_jspx_meth_fmt_005fparam_005f8(_jspx_th_fmt_005fmessage_005f15, _jspx_page_context))
          return true;
        int evalDoAfterBody = _jspx_th_fmt_005fmessage_005f15.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_fmt_005fmessage_005f15 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.popBody();
      }
    }
    if (_jspx_th_fmt_005fmessage_005f15.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey.reuse(_jspx_th_fmt_005fmessage_005f15);
      return true;
    }
    _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey.reuse(_jspx_th_fmt_005fmessage_005f15);
    return false;
  }

  private boolean _jspx_meth_fmt_005fparam_005f7(javax.servlet.jsp.tagext.JspTag _jspx_th_fmt_005fmessage_005f15, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:param
    org.apache.taglibs.standard.tag.rt.fmt.ParamTag _jspx_th_fmt_005fparam_005f7 = (org.apache.taglibs.standard.tag.rt.fmt.ParamTag) _005fjspx_005ftagPool_005ffmt_005fparam_0026_005fvalue_005fnobody.get(org.apache.taglibs.standard.tag.rt.fmt.ParamTag.class);
    _jspx_th_fmt_005fparam_005f7.setPageContext(_jspx_page_context);
    _jspx_th_fmt_005fparam_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_fmt_005fmessage_005f15);
    // /WEB-INF/jsp/category-include.jsp(23,72) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_fmt_005fparam_005f7.setValue((java.lang.Object) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${numSubCategories}", java.lang.Object.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_fmt_005fparam_005f7 = _jspx_th_fmt_005fparam_005f7.doStartTag();
    if (_jspx_th_fmt_005fparam_005f7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005ffmt_005fparam_0026_005fvalue_005fnobody.reuse(_jspx_th_fmt_005fparam_005f7);
      return true;
    }
    _005fjspx_005ftagPool_005ffmt_005fparam_0026_005fvalue_005fnobody.reuse(_jspx_th_fmt_005fparam_005f7);
    return false;
  }

  private boolean _jspx_meth_fmt_005fparam_005f8(javax.servlet.jsp.tagext.JspTag _jspx_th_fmt_005fmessage_005f15, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:param
    org.apache.taglibs.standard.tag.rt.fmt.ParamTag _jspx_th_fmt_005fparam_005f8 = (org.apache.taglibs.standard.tag.rt.fmt.ParamTag) _005fjspx_005ftagPool_005ffmt_005fparam_0026_005fvalue_005fnobody.get(org.apache.taglibs.standard.tag.rt.fmt.ParamTag.class);
    _jspx_th_fmt_005fparam_005f8.setPageContext(_jspx_page_context);
    _jspx_th_fmt_005fparam_005f8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_fmt_005fmessage_005f15);
    // /WEB-INF/jsp/category-include.jsp(23,113) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_fmt_005fparam_005f8.setValue((java.lang.Object) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${categoryName}", java.lang.Object.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_fmt_005fparam_005f8 = _jspx_th_fmt_005fparam_005f8.doStartTag();
    if (_jspx_th_fmt_005fparam_005f8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005ffmt_005fparam_0026_005fvalue_005fnobody.reuse(_jspx_th_fmt_005fparam_005f8);
      return true;
    }
    _005fjspx_005ftagPool_005ffmt_005fparam_0026_005fvalue_005fnobody.reuse(_jspx_th_fmt_005fparam_005f8);
    return false;
  }

  private boolean _jspx_meth_c_005fset_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f13, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:set
    org.apache.taglibs.standard.tag.rt.core.SetTag _jspx_th_c_005fset_005f0 = (org.apache.taglibs.standard.tag.rt.core.SetTag) _005fjspx_005ftagPool_005fc_005fset_0026_005fvar_005fvalue_005fnobody.get(org.apache.taglibs.standard.tag.rt.core.SetTag.class);
    _jspx_th_c_005fset_005f0.setPageContext(_jspx_page_context);
    _jspx_th_c_005fset_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f13);
    // /WEB-INF/jsp/category-include.jsp(27,2) name = var type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fset_005f0.setVar("columnCount");
    // /WEB-INF/jsp/category-include.jsp(27,2) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fset_005f0.setValue(new String("1"));
    int _jspx_eval_c_005fset_005f0 = _jspx_th_c_005fset_005f0.doStartTag();
    if (_jspx_th_c_005fset_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fset_0026_005fvar_005fvalue_005fnobody.reuse(_jspx_th_c_005fset_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fset_0026_005fvar_005fvalue_005fnobody.reuse(_jspx_th_c_005fset_005f0);
    return false;
  }

  private boolean _jspx_meth_c_005fforEach_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f13, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:forEach
    org.apache.taglibs.standard.tag.rt.core.ForEachTag _jspx_th_c_005fforEach_005f1 = (org.apache.taglibs.standard.tag.rt.core.ForEachTag) _005fjspx_005ftagPool_005fc_005fforEach_0026_005fvarStatus_005fvar_005fitems.get(org.apache.taglibs.standard.tag.rt.core.ForEachTag.class);
    _jspx_th_c_005fforEach_005f1.setPageContext(_jspx_page_context);
    _jspx_th_c_005fforEach_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f13);
    // /WEB-INF/jsp/category-include.jsp(28,2) name = items type = java.lang.Object reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fforEach_005f1.setItems((java.lang.Object) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${subCategories}", java.lang.Object.class, (PageContext)_jspx_page_context, null, false));
    // /WEB-INF/jsp/category-include.jsp(28,2) name = var type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fforEach_005f1.setVar("subCategory");
    // /WEB-INF/jsp/category-include.jsp(28,2) name = varStatus type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fforEach_005f1.setVarStatus("status");
    int[] _jspx_push_body_count_c_005fforEach_005f1 = new int[] { 0 };
    try {
      int _jspx_eval_c_005fforEach_005f1 = _jspx_th_c_005fforEach_005f1.doStartTag();
      if (_jspx_eval_c_005fforEach_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        do {
          out.write("\r\n");
          out.write("<li>");
          if (_jspx_meth_jamwiki_005flink_005f11(_jspx_th_c_005fforEach_005f1, _jspx_page_context, _jspx_push_body_count_c_005fforEach_005f1))
            return true;
          out.write("</li>\r\n");
          out.write("\t\t\t");
          out.write("\r\n");
          out.write("\t\t\t");
          if (_jspx_meth_c_005fif_005f14(_jspx_th_c_005fforEach_005f1, _jspx_page_context, _jspx_push_body_count_c_005fforEach_005f1))
            return true;
          out.write("\r\n");
          out.write("\t\t");
          int evalDoAfterBody = _jspx_th_c_005fforEach_005f1.doAfterBody();
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
      }
      if (_jspx_th_c_005fforEach_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        return true;
      }
    } catch (Throwable _jspx_exception) {
      while (_jspx_push_body_count_c_005fforEach_005f1[0]-- > 0)
        out = _jspx_page_context.popBody();
      _jspx_th_c_005fforEach_005f1.doCatch(_jspx_exception);
    } finally {
      _jspx_th_c_005fforEach_005f1.doFinally();
      _005fjspx_005ftagPool_005fc_005fforEach_0026_005fvarStatus_005fvar_005fitems.reuse(_jspx_th_c_005fforEach_005f1);
    }
    return false;
  }

  private boolean _jspx_meth_jamwiki_005flink_005f11(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fforEach_005f1, PageContext _jspx_page_context, int[] _jspx_push_body_count_c_005fforEach_005f1)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  jamwiki:link
    org.jamwiki.taglib.LinkTag _jspx_th_jamwiki_005flink_005f11 = (org.jamwiki.taglib.LinkTag) _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvalue_005ftext_005fnobody.get(org.jamwiki.taglib.LinkTag.class);
    _jspx_th_jamwiki_005flink_005f11.setPageContext(_jspx_page_context);
    _jspx_th_jamwiki_005flink_005f11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fforEach_005f1);
    // /WEB-INF/jsp/category-include.jsp(29,4) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005flink_005f11.setValue((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${subCategory.key}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
    // /WEB-INF/jsp/category-include.jsp(29,4) name = text type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005flink_005f11.setText((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${subCategory.value}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_jamwiki_005flink_005f11 = _jspx_th_jamwiki_005flink_005f11.doStartTag();
    if (_jspx_th_jamwiki_005flink_005f11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvalue_005ftext_005fnobody.reuse(_jspx_th_jamwiki_005flink_005f11);
      return true;
    }
    _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvalue_005ftext_005fnobody.reuse(_jspx_th_jamwiki_005flink_005f11);
    return false;
  }

  private boolean _jspx_meth_c_005fif_005f14(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fforEach_005f1, PageContext _jspx_page_context, int[] _jspx_push_body_count_c_005fforEach_005f1)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:if
    org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f14 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
    _jspx_th_c_005fif_005f14.setPageContext(_jspx_page_context);
    _jspx_th_c_005fif_005f14.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fforEach_005f1);
    // /WEB-INF/jsp/category-include.jsp(31,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fif_005f14.setTest(((java.lang.Boolean) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${(numSubCategories > 9) && (columnCount < 3) && ((status.count * 3) >= (numSubCategories * columnCount))}", java.lang.Boolean.class, (PageContext)_jspx_page_context, null, false)).booleanValue());
    int _jspx_eval_c_005fif_005f14 = _jspx_th_c_005fif_005f14.doStartTag();
    if (_jspx_eval_c_005fif_005f14 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("\r\n");
        out.write("\t\t\t\t");
        if (_jspx_meth_c_005fset_005f1(_jspx_th_c_005fif_005f14, _jspx_page_context, _jspx_push_body_count_c_005fforEach_005f1))
          return true;
        out.write("\r\n");
        out.write("</ul></td><td><ul>\r\n");
        out.write("\t\t\t");
        int evalDoAfterBody = _jspx_th_c_005fif_005f14.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_c_005fif_005f14.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f14);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f14);
    return false;
  }

  private boolean _jspx_meth_c_005fset_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f14, PageContext _jspx_page_context, int[] _jspx_push_body_count_c_005fforEach_005f1)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:set
    org.apache.taglibs.standard.tag.rt.core.SetTag _jspx_th_c_005fset_005f1 = (org.apache.taglibs.standard.tag.rt.core.SetTag) _005fjspx_005ftagPool_005fc_005fset_0026_005fvar_005fvalue_005fnobody.get(org.apache.taglibs.standard.tag.rt.core.SetTag.class);
    _jspx_th_c_005fset_005f1.setPageContext(_jspx_page_context);
    _jspx_th_c_005fset_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f14);
    // /WEB-INF/jsp/category-include.jsp(32,4) name = var type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fset_005f1.setVar("columnCount");
    // /WEB-INF/jsp/category-include.jsp(32,4) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fset_005f1.setValue((java.lang.Object) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${columnCount + 1}", java.lang.Object.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_c_005fset_005f1 = _jspx_th_c_005fset_005f1.doStartTag();
    if (_jspx_th_c_005fset_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fset_0026_005fvar_005fvalue_005fnobody.reuse(_jspx_th_c_005fset_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fset_0026_005fvar_005fvalue_005fnobody.reuse(_jspx_th_c_005fset_005f1);
    return false;
  }

  private boolean _jspx_meth_c_005fif_005f15(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f12, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:if
    org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f15 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
    _jspx_th_c_005fif_005f15.setPageContext(_jspx_page_context);
    _jspx_th_c_005fif_005f15.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f12);
    // /WEB-INF/jsp/category-include.jsp(41,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fif_005f15.setTest(((java.lang.Boolean) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${numCategoryImages > 0}", java.lang.Boolean.class, (PageContext)_jspx_page_context, null, false)).booleanValue());
    int _jspx_eval_c_005fif_005f15 = _jspx_th_c_005fif_005f15.doStartTag();
    if (_jspx_eval_c_005fif_005f15 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("\r\n");
        out.write("<h3>");
        if (_jspx_meth_fmt_005fmessage_005f16(_jspx_th_c_005fif_005f15, _jspx_page_context))
          return true;
        out.write("</h3>\r\n");
        out.write("<div class=\"message\">");
        if (_jspx_meth_fmt_005fmessage_005f17(_jspx_th_c_005fif_005f15, _jspx_page_context))
          return true;
        out.write("</div>\r\n");
        out.write("\r\n");
        out.write("<table class=\"gallery-cat gallery\" cellpadding=\"0\" cellspacing=\"0\"><tr>\r\n");
        out.write("\t\t");
        out.write("\r\n");
        out.write("\t\t");
        if (_jspx_meth_c_005fforEach_005f2(_jspx_th_c_005fif_005f15, _jspx_page_context))
          return true;
        out.write("\r\n");
        out.write("\t\t");
        if (_jspx_meth_c_005fif_005f17(_jspx_th_c_005fif_005f15, _jspx_page_context))
          return true;
        out.write("\r\n");
        out.write("</tr></table>\r\n");
        out.write("\t");
        int evalDoAfterBody = _jspx_th_c_005fif_005f15.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_c_005fif_005f15.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f15);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f15);
    return false;
  }

  private boolean _jspx_meth_fmt_005fmessage_005f16(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f15, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_005fmessage_005f16 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_005fmessage_005f16.setPageContext(_jspx_page_context);
    _jspx_th_fmt_005fmessage_005f16.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f15);
    // /WEB-INF/jsp/category-include.jsp(42,4) name = key type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_fmt_005fmessage_005f16.setKey("topic.category.images");
    int _jspx_eval_fmt_005fmessage_005f16 = _jspx_th_fmt_005fmessage_005f16.doStartTag();
    if (_jspx_eval_fmt_005fmessage_005f16 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_fmt_005fmessage_005f16 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_fmt_005fmessage_005f16.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_fmt_005fmessage_005f16.doInitBody();
      }
      do {
        if (_jspx_meth_fmt_005fparam_005f9(_jspx_th_fmt_005fmessage_005f16, _jspx_page_context))
          return true;
        int evalDoAfterBody = _jspx_th_fmt_005fmessage_005f16.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_fmt_005fmessage_005f16 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.popBody();
      }
    }
    if (_jspx_th_fmt_005fmessage_005f16.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey.reuse(_jspx_th_fmt_005fmessage_005f16);
      return true;
    }
    _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey.reuse(_jspx_th_fmt_005fmessage_005f16);
    return false;
  }

  private boolean _jspx_meth_fmt_005fparam_005f9(javax.servlet.jsp.tagext.JspTag _jspx_th_fmt_005fmessage_005f16, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:param
    org.apache.taglibs.standard.tag.rt.fmt.ParamTag _jspx_th_fmt_005fparam_005f9 = (org.apache.taglibs.standard.tag.rt.fmt.ParamTag) _005fjspx_005ftagPool_005ffmt_005fparam_0026_005fvalue_005fnobody.get(org.apache.taglibs.standard.tag.rt.fmt.ParamTag.class);
    _jspx_th_fmt_005fparam_005f9.setPageContext(_jspx_page_context);
    _jspx_th_fmt_005fparam_005f9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_fmt_005fmessage_005f16);
    // /WEB-INF/jsp/category-include.jsp(42,45) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_fmt_005fparam_005f9.setValue((java.lang.Object) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${categoryName}", java.lang.Object.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_fmt_005fparam_005f9 = _jspx_th_fmt_005fparam_005f9.doStartTag();
    if (_jspx_th_fmt_005fparam_005f9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005ffmt_005fparam_0026_005fvalue_005fnobody.reuse(_jspx_th_fmt_005fparam_005f9);
      return true;
    }
    _005fjspx_005ftagPool_005ffmt_005fparam_0026_005fvalue_005fnobody.reuse(_jspx_th_fmt_005fparam_005f9);
    return false;
  }

  private boolean _jspx_meth_fmt_005fmessage_005f17(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f15, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_005fmessage_005f17 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_005fmessage_005f17.setPageContext(_jspx_page_context);
    _jspx_th_fmt_005fmessage_005f17.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f15);
    // /WEB-INF/jsp/category-include.jsp(43,21) name = key type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_fmt_005fmessage_005f17.setKey("topic.category.numimages");
    int _jspx_eval_fmt_005fmessage_005f17 = _jspx_th_fmt_005fmessage_005f17.doStartTag();
    if (_jspx_eval_fmt_005fmessage_005f17 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_fmt_005fmessage_005f17 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_fmt_005fmessage_005f17.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_fmt_005fmessage_005f17.doInitBody();
      }
      do {
        if (_jspx_meth_fmt_005fparam_005f10(_jspx_th_fmt_005fmessage_005f17, _jspx_page_context))
          return true;
        if (_jspx_meth_fmt_005fparam_005f11(_jspx_th_fmt_005fmessage_005f17, _jspx_page_context))
          return true;
        int evalDoAfterBody = _jspx_th_fmt_005fmessage_005f17.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_fmt_005fmessage_005f17 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.popBody();
      }
    }
    if (_jspx_th_fmt_005fmessage_005f17.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey.reuse(_jspx_th_fmt_005fmessage_005f17);
      return true;
    }
    _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey.reuse(_jspx_th_fmt_005fmessage_005f17);
    return false;
  }

  private boolean _jspx_meth_fmt_005fparam_005f10(javax.servlet.jsp.tagext.JspTag _jspx_th_fmt_005fmessage_005f17, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:param
    org.apache.taglibs.standard.tag.rt.fmt.ParamTag _jspx_th_fmt_005fparam_005f10 = (org.apache.taglibs.standard.tag.rt.fmt.ParamTag) _005fjspx_005ftagPool_005ffmt_005fparam_0026_005fvalue_005fnobody.get(org.apache.taglibs.standard.tag.rt.fmt.ParamTag.class);
    _jspx_th_fmt_005fparam_005f10.setPageContext(_jspx_page_context);
    _jspx_th_fmt_005fparam_005f10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_fmt_005fmessage_005f17);
    // /WEB-INF/jsp/category-include.jsp(43,65) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_fmt_005fparam_005f10.setValue((java.lang.Object) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${numCategoryImages}", java.lang.Object.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_fmt_005fparam_005f10 = _jspx_th_fmt_005fparam_005f10.doStartTag();
    if (_jspx_th_fmt_005fparam_005f10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005ffmt_005fparam_0026_005fvalue_005fnobody.reuse(_jspx_th_fmt_005fparam_005f10);
      return true;
    }
    _005fjspx_005ftagPool_005ffmt_005fparam_0026_005fvalue_005fnobody.reuse(_jspx_th_fmt_005fparam_005f10);
    return false;
  }

  private boolean _jspx_meth_fmt_005fparam_005f11(javax.servlet.jsp.tagext.JspTag _jspx_th_fmt_005fmessage_005f17, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:param
    org.apache.taglibs.standard.tag.rt.fmt.ParamTag _jspx_th_fmt_005fparam_005f11 = (org.apache.taglibs.standard.tag.rt.fmt.ParamTag) _005fjspx_005ftagPool_005ffmt_005fparam_0026_005fvalue_005fnobody.get(org.apache.taglibs.standard.tag.rt.fmt.ParamTag.class);
    _jspx_th_fmt_005fparam_005f11.setPageContext(_jspx_page_context);
    _jspx_th_fmt_005fparam_005f11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_fmt_005fmessage_005f17);
    // /WEB-INF/jsp/category-include.jsp(43,107) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_fmt_005fparam_005f11.setValue((java.lang.Object) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${categoryName}", java.lang.Object.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_fmt_005fparam_005f11 = _jspx_th_fmt_005fparam_005f11.doStartTag();
    if (_jspx_th_fmt_005fparam_005f11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005ffmt_005fparam_0026_005fvalue_005fnobody.reuse(_jspx_th_fmt_005fparam_005f11);
      return true;
    }
    _005fjspx_005ftagPool_005ffmt_005fparam_0026_005fvalue_005fnobody.reuse(_jspx_th_fmt_005fparam_005f11);
    return false;
  }

  private boolean _jspx_meth_c_005fforEach_005f2(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f15, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:forEach
    org.apache.taglibs.standard.tag.rt.core.ForEachTag _jspx_th_c_005fforEach_005f2 = (org.apache.taglibs.standard.tag.rt.core.ForEachTag) _005fjspx_005ftagPool_005fc_005fforEach_0026_005fvarStatus_005fvar_005fitems.get(org.apache.taglibs.standard.tag.rt.core.ForEachTag.class);
    _jspx_th_c_005fforEach_005f2.setPageContext(_jspx_page_context);
    _jspx_th_c_005fforEach_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f15);
    // /WEB-INF/jsp/category-include.jsp(47,2) name = items type = java.lang.Object reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fforEach_005f2.setItems((java.lang.Object) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${categoryImages}", java.lang.Object.class, (PageContext)_jspx_page_context, null, false));
    // /WEB-INF/jsp/category-include.jsp(47,2) name = var type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fforEach_005f2.setVar("categoryImage");
    // /WEB-INF/jsp/category-include.jsp(47,2) name = varStatus type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fforEach_005f2.setVarStatus("status");
    int[] _jspx_push_body_count_c_005fforEach_005f2 = new int[] { 0 };
    try {
      int _jspx_eval_c_005fforEach_005f2 = _jspx_th_c_005fforEach_005f2.doStartTag();
      if (_jspx_eval_c_005fforEach_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        do {
          out.write("\r\n");
          out.write("\t\t\t");
          if (_jspx_meth_c_005fif_005f16(_jspx_th_c_005fforEach_005f2, _jspx_page_context, _jspx_push_body_count_c_005fforEach_005f2))
            return true;
          out.write("\r\n");
          out.write("<td><div class=\"gallerybox\"><a href=\"");
          if (_jspx_meth_jamwiki_005flink_005f12(_jspx_th_c_005fforEach_005f2, _jspx_page_context, _jspx_push_body_count_c_005fforEach_005f2))
            return true;
          out.write("\" class=\"wikiimg\">");
          if (_jspx_meth_jamwiki_005fimage_005f1(_jspx_th_c_005fforEach_005f2, _jspx_page_context, _jspx_push_body_count_c_005fforEach_005f2))
            return true;
          out.write("</a></div></td>\r\n");
          out.write("\t\t");
          int evalDoAfterBody = _jspx_th_c_005fforEach_005f2.doAfterBody();
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
      }
      if (_jspx_th_c_005fforEach_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        return true;
      }
    } catch (Throwable _jspx_exception) {
      while (_jspx_push_body_count_c_005fforEach_005f2[0]-- > 0)
        out = _jspx_page_context.popBody();
      _jspx_th_c_005fforEach_005f2.doCatch(_jspx_exception);
    } finally {
      _jspx_th_c_005fforEach_005f2.doFinally();
      _005fjspx_005ftagPool_005fc_005fforEach_0026_005fvarStatus_005fvar_005fitems.reuse(_jspx_th_c_005fforEach_005f2);
    }
    return false;
  }

  private boolean _jspx_meth_c_005fif_005f16(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fforEach_005f2, PageContext _jspx_page_context, int[] _jspx_push_body_count_c_005fforEach_005f2)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:if
    org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f16 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
    _jspx_th_c_005fif_005f16.setPageContext(_jspx_page_context);
    _jspx_th_c_005fif_005f16.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fforEach_005f2);
    // /WEB-INF/jsp/category-include.jsp(48,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fif_005f16.setTest(((java.lang.Boolean) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${status.count != 1 && (status.count % 4) == 1}", java.lang.Boolean.class, (PageContext)_jspx_page_context, null, false)).booleanValue());
    int _jspx_eval_c_005fif_005f16 = _jspx_th_c_005fif_005f16.doStartTag();
    if (_jspx_eval_c_005fif_005f16 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("\r\n");
        out.write("</tr><tr>\r\n");
        out.write("\t\t\t");
        int evalDoAfterBody = _jspx_th_c_005fif_005f16.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_c_005fif_005f16.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f16);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f16);
    return false;
  }

  private boolean _jspx_meth_jamwiki_005flink_005f12(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fforEach_005f2, PageContext _jspx_page_context, int[] _jspx_push_body_count_c_005fforEach_005f2)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  jamwiki:link
    org.jamwiki.taglib.LinkTag _jspx_th_jamwiki_005flink_005f12 = (org.jamwiki.taglib.LinkTag) _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvalue_005fnobody.get(org.jamwiki.taglib.LinkTag.class);
    _jspx_th_jamwiki_005flink_005f12.setPageContext(_jspx_page_context);
    _jspx_th_jamwiki_005flink_005f12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fforEach_005f2);
    // /WEB-INF/jsp/category-include.jsp(51,37) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005flink_005f12.setValue((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${categoryImage.childTopicName}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_jamwiki_005flink_005f12 = _jspx_th_jamwiki_005flink_005f12.doStartTag();
    if (_jspx_th_jamwiki_005flink_005f12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvalue_005fnobody.reuse(_jspx_th_jamwiki_005flink_005f12);
      return true;
    }
    _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvalue_005fnobody.reuse(_jspx_th_jamwiki_005flink_005f12);
    return false;
  }

  private boolean _jspx_meth_jamwiki_005fimage_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fforEach_005f2, PageContext _jspx_page_context, int[] _jspx_push_body_count_c_005fforEach_005f2)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  jamwiki:image
    org.jamwiki.taglib.ImageLinkTag _jspx_th_jamwiki_005fimage_005f1 = (org.jamwiki.taglib.ImageLinkTag) _005fjspx_005ftagPool_005fjamwiki_005fimage_0026_005fvalue_005fstyle_005fmaxWidth_005fmaxHeight_005fnobody.get(org.jamwiki.taglib.ImageLinkTag.class);
    _jspx_th_jamwiki_005fimage_005f1.setPageContext(_jspx_page_context);
    _jspx_th_jamwiki_005fimage_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fforEach_005f2);
    // /WEB-INF/jsp/category-include.jsp(51,111) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005fimage_005f1.setValue((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${categoryImage.childTopicName}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
    // /WEB-INF/jsp/category-include.jsp(51,111) name = maxWidth type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005fimage_005f1.setMaxWidth("120");
    // /WEB-INF/jsp/category-include.jsp(51,111) name = maxHeight type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005fimage_005f1.setMaxHeight("120");
    // /WEB-INF/jsp/category-include.jsp(51,111) name = style type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005fimage_005f1.setStyle("gallery");
    int _jspx_eval_jamwiki_005fimage_005f1 = _jspx_th_jamwiki_005fimage_005f1.doStartTag();
    if (_jspx_th_jamwiki_005fimage_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fjamwiki_005fimage_0026_005fvalue_005fstyle_005fmaxWidth_005fmaxHeight_005fnobody.reuse(_jspx_th_jamwiki_005fimage_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fjamwiki_005fimage_0026_005fvalue_005fstyle_005fmaxWidth_005fmaxHeight_005fnobody.reuse(_jspx_th_jamwiki_005fimage_005f1);
    return false;
  }

  private boolean _jspx_meth_c_005fif_005f17(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f15, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:if
    org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f17 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
    _jspx_th_c_005fif_005f17.setPageContext(_jspx_page_context);
    _jspx_th_c_005fif_005f17.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f15);
    // /WEB-INF/jsp/category-include.jsp(53,2) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fif_005f17.setTest(((java.lang.Boolean) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${(numCategoryImages % 4) != 0}", java.lang.Boolean.class, (PageContext)_jspx_page_context, null, false)).booleanValue());
    int _jspx_eval_c_005fif_005f17 = _jspx_th_c_005fif_005f17.doStartTag();
    if (_jspx_eval_c_005fif_005f17 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("\r\n");
        out.write("\t\t\t");
        if (_jspx_meth_c_005fforEach_005f3(_jspx_th_c_005fif_005f17, _jspx_page_context))
          return true;
        out.write("\r\n");
        out.write("\t\t");
        int evalDoAfterBody = _jspx_th_c_005fif_005f17.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_c_005fif_005f17.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f17);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f17);
    return false;
  }

  private boolean _jspx_meth_c_005fforEach_005f3(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f17, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:forEach
    org.apache.taglibs.standard.tag.rt.core.ForEachTag _jspx_th_c_005fforEach_005f3 = (org.apache.taglibs.standard.tag.rt.core.ForEachTag) _005fjspx_005ftagPool_005fc_005fforEach_0026_005fend_005fbegin.get(org.apache.taglibs.standard.tag.rt.core.ForEachTag.class);
    _jspx_th_c_005fforEach_005f3.setPageContext(_jspx_page_context);
    _jspx_th_c_005fforEach_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f17);
    // /WEB-INF/jsp/category-include.jsp(54,3) name = begin type = int reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fforEach_005f3.setBegin(1);
    // /WEB-INF/jsp/category-include.jsp(54,3) name = end type = int reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fforEach_005f3.setEnd(((java.lang.Integer) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${4 - (numCategoryImages % 4)}", java.lang.Integer.class, (PageContext)_jspx_page_context, null, false)).intValue());
    int[] _jspx_push_body_count_c_005fforEach_005f3 = new int[] { 0 };
    try {
      int _jspx_eval_c_005fforEach_005f3 = _jspx_th_c_005fforEach_005f3.doStartTag();
      if (_jspx_eval_c_005fforEach_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        do {
          out.write("\r\n");
          out.write("<td>&#160;</td>\r\n");
          out.write("\t\t\t");
          int evalDoAfterBody = _jspx_th_c_005fforEach_005f3.doAfterBody();
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
      }
      if (_jspx_th_c_005fforEach_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        return true;
      }
    } catch (Throwable _jspx_exception) {
      while (_jspx_push_body_count_c_005fforEach_005f3[0]-- > 0)
        out = _jspx_page_context.popBody();
      _jspx_th_c_005fforEach_005f3.doCatch(_jspx_exception);
    } finally {
      _jspx_th_c_005fforEach_005f3.doFinally();
      _005fjspx_005ftagPool_005fc_005fforEach_0026_005fend_005fbegin.reuse(_jspx_th_c_005fforEach_005f3);
    }
    return false;
  }

  private boolean _jspx_meth_fmt_005fmessage_005f18(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f12, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_005fmessage_005f18 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_005fmessage_005f18.setPageContext(_jspx_page_context);
    _jspx_th_fmt_005fmessage_005f18.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f12);
    // /WEB-INF/jsp/category-include.jsp(61,4) name = key type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_fmt_005fmessage_005f18.setKey("topic.category.topics");
    int _jspx_eval_fmt_005fmessage_005f18 = _jspx_th_fmt_005fmessage_005f18.doStartTag();
    if (_jspx_eval_fmt_005fmessage_005f18 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_fmt_005fmessage_005f18 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_fmt_005fmessage_005f18.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_fmt_005fmessage_005f18.doInitBody();
      }
      do {
        if (_jspx_meth_fmt_005fparam_005f12(_jspx_th_fmt_005fmessage_005f18, _jspx_page_context))
          return true;
        int evalDoAfterBody = _jspx_th_fmt_005fmessage_005f18.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_fmt_005fmessage_005f18 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.popBody();
      }
    }
    if (_jspx_th_fmt_005fmessage_005f18.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey.reuse(_jspx_th_fmt_005fmessage_005f18);
      return true;
    }
    _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey.reuse(_jspx_th_fmt_005fmessage_005f18);
    return false;
  }

  private boolean _jspx_meth_fmt_005fparam_005f12(javax.servlet.jsp.tagext.JspTag _jspx_th_fmt_005fmessage_005f18, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:param
    org.apache.taglibs.standard.tag.rt.fmt.ParamTag _jspx_th_fmt_005fparam_005f12 = (org.apache.taglibs.standard.tag.rt.fmt.ParamTag) _005fjspx_005ftagPool_005ffmt_005fparam_0026_005fvalue_005fnobody.get(org.apache.taglibs.standard.tag.rt.fmt.ParamTag.class);
    _jspx_th_fmt_005fparam_005f12.setPageContext(_jspx_page_context);
    _jspx_th_fmt_005fparam_005f12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_fmt_005fmessage_005f18);
    // /WEB-INF/jsp/category-include.jsp(61,45) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_fmt_005fparam_005f12.setValue((java.lang.Object) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${categoryName}", java.lang.Object.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_fmt_005fparam_005f12 = _jspx_th_fmt_005fparam_005f12.doStartTag();
    if (_jspx_th_fmt_005fparam_005f12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005ffmt_005fparam_0026_005fvalue_005fnobody.reuse(_jspx_th_fmt_005fparam_005f12);
      return true;
    }
    _005fjspx_005ftagPool_005ffmt_005fparam_0026_005fvalue_005fnobody.reuse(_jspx_th_fmt_005fparam_005f12);
    return false;
  }

  private boolean _jspx_meth_fmt_005fmessage_005f19(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f12, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_005fmessage_005f19 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_005fmessage_005f19.setPageContext(_jspx_page_context);
    _jspx_th_fmt_005fmessage_005f19.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f12);
    // /WEB-INF/jsp/category-include.jsp(63,21) name = key type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_fmt_005fmessage_005f19.setKey("topic.category.numtopics");
    int _jspx_eval_fmt_005fmessage_005f19 = _jspx_th_fmt_005fmessage_005f19.doStartTag();
    if (_jspx_eval_fmt_005fmessage_005f19 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_fmt_005fmessage_005f19 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_fmt_005fmessage_005f19.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_fmt_005fmessage_005f19.doInitBody();
      }
      do {
        if (_jspx_meth_fmt_005fparam_005f13(_jspx_th_fmt_005fmessage_005f19, _jspx_page_context))
          return true;
        if (_jspx_meth_fmt_005fparam_005f14(_jspx_th_fmt_005fmessage_005f19, _jspx_page_context))
          return true;
        int evalDoAfterBody = _jspx_th_fmt_005fmessage_005f19.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_fmt_005fmessage_005f19 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.popBody();
      }
    }
    if (_jspx_th_fmt_005fmessage_005f19.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey.reuse(_jspx_th_fmt_005fmessage_005f19);
      return true;
    }
    _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey.reuse(_jspx_th_fmt_005fmessage_005f19);
    return false;
  }

  private boolean _jspx_meth_fmt_005fparam_005f13(javax.servlet.jsp.tagext.JspTag _jspx_th_fmt_005fmessage_005f19, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:param
    org.apache.taglibs.standard.tag.rt.fmt.ParamTag _jspx_th_fmt_005fparam_005f13 = (org.apache.taglibs.standard.tag.rt.fmt.ParamTag) _005fjspx_005ftagPool_005ffmt_005fparam_0026_005fvalue_005fnobody.get(org.apache.taglibs.standard.tag.rt.fmt.ParamTag.class);
    _jspx_th_fmt_005fparam_005f13.setPageContext(_jspx_page_context);
    _jspx_th_fmt_005fparam_005f13.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_fmt_005fmessage_005f19);
    // /WEB-INF/jsp/category-include.jsp(63,65) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_fmt_005fparam_005f13.setValue((java.lang.Object) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${numCategoryTopics}", java.lang.Object.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_fmt_005fparam_005f13 = _jspx_th_fmt_005fparam_005f13.doStartTag();
    if (_jspx_th_fmt_005fparam_005f13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005ffmt_005fparam_0026_005fvalue_005fnobody.reuse(_jspx_th_fmt_005fparam_005f13);
      return true;
    }
    _005fjspx_005ftagPool_005ffmt_005fparam_0026_005fvalue_005fnobody.reuse(_jspx_th_fmt_005fparam_005f13);
    return false;
  }

  private boolean _jspx_meth_fmt_005fparam_005f14(javax.servlet.jsp.tagext.JspTag _jspx_th_fmt_005fmessage_005f19, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:param
    org.apache.taglibs.standard.tag.rt.fmt.ParamTag _jspx_th_fmt_005fparam_005f14 = (org.apache.taglibs.standard.tag.rt.fmt.ParamTag) _005fjspx_005ftagPool_005ffmt_005fparam_0026_005fvalue_005fnobody.get(org.apache.taglibs.standard.tag.rt.fmt.ParamTag.class);
    _jspx_th_fmt_005fparam_005f14.setPageContext(_jspx_page_context);
    _jspx_th_fmt_005fparam_005f14.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_fmt_005fmessage_005f19);
    // /WEB-INF/jsp/category-include.jsp(63,107) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_fmt_005fparam_005f14.setValue((java.lang.Object) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${categoryName}", java.lang.Object.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_fmt_005fparam_005f14 = _jspx_th_fmt_005fparam_005f14.doStartTag();
    if (_jspx_th_fmt_005fparam_005f14.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005ffmt_005fparam_0026_005fvalue_005fnobody.reuse(_jspx_th_fmt_005fparam_005f14);
      return true;
    }
    _005fjspx_005ftagPool_005ffmt_005fparam_0026_005fvalue_005fnobody.reuse(_jspx_th_fmt_005fparam_005f14);
    return false;
  }

  private boolean _jspx_meth_c_005fif_005f18(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f12, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:if
    org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f18 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
    _jspx_th_c_005fif_005f18.setPageContext(_jspx_page_context);
    _jspx_th_c_005fif_005f18.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f12);
    // /WEB-INF/jsp/category-include.jsp(64,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fif_005f18.setTest(((java.lang.Boolean) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${numCategoryTopics > 0}", java.lang.Boolean.class, (PageContext)_jspx_page_context, null, false)).booleanValue());
    int _jspx_eval_c_005fif_005f18 = _jspx_th_c_005fif_005f18.doStartTag();
    if (_jspx_eval_c_005fif_005f18 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("\r\n");
        out.write("<div class=\"message\">");
        if (_jspx_meth_fmt_005fmessage_005f20(_jspx_th_c_005fif_005f18, _jspx_page_context))
          return true;
        out.write(':');
        out.write(' ');
        if (_jspx_meth_jamwiki_005fpagination_005f0(_jspx_th_c_005fif_005f18, _jspx_page_context))
          return true;
        out.write("</div>\r\n");
        out.write("<table class=\"categories\"><tr><td>\r\n");
        out.write("<ul>\r\n");
        out.write("\t\t");
        if (_jspx_meth_c_005fset_005f2(_jspx_th_c_005fif_005f18, _jspx_page_context))
          return true;
        out.write("\r\n");
        out.write("\t\t");
        if (_jspx_meth_c_005fforEach_005f4(_jspx_th_c_005fif_005f18, _jspx_page_context))
          return true;
        out.write("\r\n");
        out.write("</ul>\r\n");
        out.write("</td></tr></table>\r\n");
        out.write("\t");
        int evalDoAfterBody = _jspx_th_c_005fif_005f18.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_c_005fif_005f18.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f18);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f18);
    return false;
  }

  private boolean _jspx_meth_fmt_005fmessage_005f20(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f18, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_005fmessage_005f20 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_005fmessage_005f20.setPageContext(_jspx_page_context);
    _jspx_th_fmt_005fmessage_005f20.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f18);
    // /WEB-INF/jsp/category-include.jsp(65,21) name = key type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_fmt_005fmessage_005f20.setKey("common.caption.view");
    int _jspx_eval_fmt_005fmessage_005f20 = _jspx_th_fmt_005fmessage_005f20.doStartTag();
    if (_jspx_th_fmt_005fmessage_005f20.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f20);
      return true;
    }
    _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f20);
    return false;
  }

  private boolean _jspx_meth_jamwiki_005fpagination_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f18, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  jamwiki:pagination
    org.jamwiki.taglib.PaginationTag _jspx_th_jamwiki_005fpagination_005f0 = (org.jamwiki.taglib.PaginationTag) _005fjspx_005ftagPool_005fjamwiki_005fpagination_0026_005ftotal_005frootUrl_005fnobody.get(org.jamwiki.taglib.PaginationTag.class);
    _jspx_th_jamwiki_005fpagination_005f0.setPageContext(_jspx_page_context);
    _jspx_th_jamwiki_005fpagination_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f18);
    // /WEB-INF/jsp/category-include.jsp(65,64) name = total type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005fpagination_005f0.setTotal((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${displayCategoryCount}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
    // /WEB-INF/jsp/category-include.jsp(65,64) name = rootUrl type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005fpagination_005f0.setRootUrl((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageInfo.topicName}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_jamwiki_005fpagination_005f0 = _jspx_th_jamwiki_005fpagination_005f0.doStartTag();
    if (_jspx_th_jamwiki_005fpagination_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fjamwiki_005fpagination_0026_005ftotal_005frootUrl_005fnobody.reuse(_jspx_th_jamwiki_005fpagination_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fjamwiki_005fpagination_0026_005ftotal_005frootUrl_005fnobody.reuse(_jspx_th_jamwiki_005fpagination_005f0);
    return false;
  }

  private boolean _jspx_meth_c_005fset_005f2(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f18, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:set
    org.apache.taglibs.standard.tag.rt.core.SetTag _jspx_th_c_005fset_005f2 = (org.apache.taglibs.standard.tag.rt.core.SetTag) _005fjspx_005ftagPool_005fc_005fset_0026_005fvar_005fvalue_005fnobody.get(org.apache.taglibs.standard.tag.rt.core.SetTag.class);
    _jspx_th_c_005fset_005f2.setPageContext(_jspx_page_context);
    _jspx_th_c_005fset_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f18);
    // /WEB-INF/jsp/category-include.jsp(68,2) name = var type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fset_005f2.setVar("columnCount");
    // /WEB-INF/jsp/category-include.jsp(68,2) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fset_005f2.setValue(new String("1"));
    int _jspx_eval_c_005fset_005f2 = _jspx_th_c_005fset_005f2.doStartTag();
    if (_jspx_th_c_005fset_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fset_0026_005fvar_005fvalue_005fnobody.reuse(_jspx_th_c_005fset_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fset_0026_005fvar_005fvalue_005fnobody.reuse(_jspx_th_c_005fset_005f2);
    return false;
  }

  private boolean _jspx_meth_c_005fforEach_005f4(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f18, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:forEach
    org.apache.taglibs.standard.tag.rt.core.ForEachTag _jspx_th_c_005fforEach_005f4 = (org.apache.taglibs.standard.tag.rt.core.ForEachTag) _005fjspx_005ftagPool_005fc_005fforEach_0026_005fvarStatus_005fvar_005fitems.get(org.apache.taglibs.standard.tag.rt.core.ForEachTag.class);
    _jspx_th_c_005fforEach_005f4.setPageContext(_jspx_page_context);
    _jspx_th_c_005fforEach_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f18);
    // /WEB-INF/jsp/category-include.jsp(69,2) name = items type = java.lang.Object reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fforEach_005f4.setItems((java.lang.Object) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${categoryTopics}", java.lang.Object.class, (PageContext)_jspx_page_context, null, false));
    // /WEB-INF/jsp/category-include.jsp(69,2) name = var type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fforEach_005f4.setVar("subtopic");
    // /WEB-INF/jsp/category-include.jsp(69,2) name = varStatus type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fforEach_005f4.setVarStatus("status");
    int[] _jspx_push_body_count_c_005fforEach_005f4 = new int[] { 0 };
    try {
      int _jspx_eval_c_005fforEach_005f4 = _jspx_th_c_005fforEach_005f4.doStartTag();
      if (_jspx_eval_c_005fforEach_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        do {
          out.write("\r\n");
          out.write("<li>");
          if (_jspx_meth_jamwiki_005flink_005f13(_jspx_th_c_005fforEach_005f4, _jspx_page_context, _jspx_push_body_count_c_005fforEach_005f4))
            return true;
          out.write("</li>\r\n");
          out.write("\t\t\t");
          out.write("\r\n");
          out.write("\t\t\t");
          if (_jspx_meth_c_005fif_005f19(_jspx_th_c_005fforEach_005f4, _jspx_page_context, _jspx_push_body_count_c_005fforEach_005f4))
            return true;
          out.write("\r\n");
          out.write("\t\t");
          int evalDoAfterBody = _jspx_th_c_005fforEach_005f4.doAfterBody();
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
      }
      if (_jspx_th_c_005fforEach_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        return true;
      }
    } catch (Throwable _jspx_exception) {
      while (_jspx_push_body_count_c_005fforEach_005f4[0]-- > 0)
        out = _jspx_page_context.popBody();
      _jspx_th_c_005fforEach_005f4.doCatch(_jspx_exception);
    } finally {
      _jspx_th_c_005fforEach_005f4.doFinally();
      _005fjspx_005ftagPool_005fc_005fforEach_0026_005fvarStatus_005fvar_005fitems.reuse(_jspx_th_c_005fforEach_005f4);
    }
    return false;
  }

  private boolean _jspx_meth_jamwiki_005flink_005f13(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fforEach_005f4, PageContext _jspx_page_context, int[] _jspx_push_body_count_c_005fforEach_005f4)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  jamwiki:link
    org.jamwiki.taglib.LinkTag _jspx_th_jamwiki_005flink_005f13 = (org.jamwiki.taglib.LinkTag) _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvalue_005ftext_005fnobody.get(org.jamwiki.taglib.LinkTag.class);
    _jspx_th_jamwiki_005flink_005f13.setPageContext(_jspx_page_context);
    _jspx_th_jamwiki_005flink_005f13.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fforEach_005f4);
    // /WEB-INF/jsp/category-include.jsp(70,4) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005flink_005f13.setValue((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${subtopic.childTopicName}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
    // /WEB-INF/jsp/category-include.jsp(70,4) name = text type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005flink_005f13.setText((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${subtopic.childTopicName}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_jamwiki_005flink_005f13 = _jspx_th_jamwiki_005flink_005f13.doStartTag();
    if (_jspx_th_jamwiki_005flink_005f13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvalue_005ftext_005fnobody.reuse(_jspx_th_jamwiki_005flink_005f13);
      return true;
    }
    _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvalue_005ftext_005fnobody.reuse(_jspx_th_jamwiki_005flink_005f13);
    return false;
  }

  private boolean _jspx_meth_c_005fif_005f19(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fforEach_005f4, PageContext _jspx_page_context, int[] _jspx_push_body_count_c_005fforEach_005f4)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:if
    org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f19 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
    _jspx_th_c_005fif_005f19.setPageContext(_jspx_page_context);
    _jspx_th_c_005fif_005f19.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fforEach_005f4);
    // /WEB-INF/jsp/category-include.jsp(72,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fif_005f19.setTest(((java.lang.Boolean) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${(displayCategoryCount > 9) && (columnCount < 3) && ((status.count * 3) >= (displayCategoryCount * columnCount))}", java.lang.Boolean.class, (PageContext)_jspx_page_context, null, false)).booleanValue());
    int _jspx_eval_c_005fif_005f19 = _jspx_th_c_005fif_005f19.doStartTag();
    if (_jspx_eval_c_005fif_005f19 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("\r\n");
        out.write("\t\t\t\t");
        if (_jspx_meth_c_005fset_005f3(_jspx_th_c_005fif_005f19, _jspx_page_context, _jspx_push_body_count_c_005fforEach_005f4))
          return true;
        out.write("\r\n");
        out.write("</ul></td><td><ul>\r\n");
        out.write("\t\t\t");
        int evalDoAfterBody = _jspx_th_c_005fif_005f19.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_c_005fif_005f19.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f19);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f19);
    return false;
  }

  private boolean _jspx_meth_c_005fset_005f3(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f19, PageContext _jspx_page_context, int[] _jspx_push_body_count_c_005fforEach_005f4)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:set
    org.apache.taglibs.standard.tag.rt.core.SetTag _jspx_th_c_005fset_005f3 = (org.apache.taglibs.standard.tag.rt.core.SetTag) _005fjspx_005ftagPool_005fc_005fset_0026_005fvar_005fvalue_005fnobody.get(org.apache.taglibs.standard.tag.rt.core.SetTag.class);
    _jspx_th_c_005fset_005f3.setPageContext(_jspx_page_context);
    _jspx_th_c_005fset_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f19);
    // /WEB-INF/jsp/category-include.jsp(73,4) name = var type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fset_005f3.setVar("columnCount");
    // /WEB-INF/jsp/category-include.jsp(73,4) name = value type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fset_005f3.setValue((java.lang.Object) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${columnCount + 1}", java.lang.Object.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_c_005fset_005f3 = _jspx_th_c_005fset_005f3.doStartTag();
    if (_jspx_th_c_005fset_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fset_0026_005fvar_005fvalue_005fnobody.reuse(_jspx_th_c_005fset_005f3);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fset_0026_005fvar_005fvalue_005fnobody.reuse(_jspx_th_c_005fset_005f3);
    return false;
  }

  private boolean _jspx_meth_c_005fif_005f20(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f11, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:if
    org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f20 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
    _jspx_th_c_005fif_005f20.setPageContext(_jspx_page_context);
    _jspx_th_c_005fif_005f20.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f11);
    // /WEB-INF/jsp/view-category-include.jsp(22,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fif_005f20.setTest(((java.lang.Boolean) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${!empty categories}", java.lang.Boolean.class, (PageContext)_jspx_page_context, null, false)).booleanValue());
    int _jspx_eval_c_005fif_005f20 = _jspx_th_c_005fif_005f20.doStartTag();
    if (_jspx_eval_c_005fif_005f20 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("\r\n");
        out.write("\t\t<div id=\"category-index\">");
        if (_jspx_meth_jamwiki_005flink_005f14(_jspx_th_c_005fif_005f20, _jspx_page_context))
          return true;
        out.write(":\r\n");
        out.write("\t\t");
        if (_jspx_meth_c_005fforEach_005f5(_jspx_th_c_005fif_005f20, _jspx_page_context))
          return true;
        out.write("\r\n");
        out.write("\t\t</div>\r\n");
        out.write("\t\t<div class=\"clear\"></div>\r\n");
        out.write("\t");
        int evalDoAfterBody = _jspx_th_c_005fif_005f20.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_c_005fif_005f20.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f20);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f20);
    return false;
  }

  private boolean _jspx_meth_jamwiki_005flink_005f14(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f20, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  jamwiki:link
    org.jamwiki.taglib.LinkTag _jspx_th_jamwiki_005flink_005f14 = (org.jamwiki.taglib.LinkTag) _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvalue.get(org.jamwiki.taglib.LinkTag.class);
    _jspx_th_jamwiki_005flink_005f14.setPageContext(_jspx_page_context);
    _jspx_th_jamwiki_005flink_005f14.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f20);
    // /WEB-INF/jsp/view-category-include.jsp(23,27) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005flink_005f14.setValue("Special:Categories");
    int _jspx_eval_jamwiki_005flink_005f14 = _jspx_th_jamwiki_005flink_005f14.doStartTag();
    if (_jspx_eval_jamwiki_005flink_005f14 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_jamwiki_005flink_005f14 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_jamwiki_005flink_005f14.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_jamwiki_005flink_005f14.doInitBody();
      }
      do {
        if (_jspx_meth_fmt_005fmessage_005f21(_jspx_th_jamwiki_005flink_005f14, _jspx_page_context))
          return true;
        int evalDoAfterBody = _jspx_th_jamwiki_005flink_005f14.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_jamwiki_005flink_005f14 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.popBody();
      }
    }
    if (_jspx_th_jamwiki_005flink_005f14.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvalue.reuse(_jspx_th_jamwiki_005flink_005f14);
      return true;
    }
    _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvalue.reuse(_jspx_th_jamwiki_005flink_005f14);
    return false;
  }

  private boolean _jspx_meth_fmt_005fmessage_005f21(javax.servlet.jsp.tagext.JspTag _jspx_th_jamwiki_005flink_005f14, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_005fmessage_005f21 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_005fmessage_005f21.setPageContext(_jspx_page_context);
    _jspx_th_fmt_005fmessage_005f21.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_jamwiki_005flink_005f14);
    // /WEB-INF/jsp/view-category-include.jsp(23,68) name = key type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_fmt_005fmessage_005f21.setKey("topic.categories");
    int _jspx_eval_fmt_005fmessage_005f21 = _jspx_th_fmt_005fmessage_005f21.doStartTag();
    if (_jspx_th_fmt_005fmessage_005f21.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f21);
      return true;
    }
    _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f21);
    return false;
  }

  private boolean _jspx_meth_c_005fforEach_005f5(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f20, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:forEach
    org.apache.taglibs.standard.tag.rt.core.ForEachTag _jspx_th_c_005fforEach_005f5 = (org.apache.taglibs.standard.tag.rt.core.ForEachTag) _005fjspx_005ftagPool_005fc_005fforEach_0026_005fvarStatus_005fvar_005fitems.get(org.apache.taglibs.standard.tag.rt.core.ForEachTag.class);
    _jspx_th_c_005fforEach_005f5.setPageContext(_jspx_page_context);
    _jspx_th_c_005fforEach_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f20);
    // /WEB-INF/jsp/view-category-include.jsp(24,2) name = items type = java.lang.Object reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fforEach_005f5.setItems((java.lang.Object) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${categories}", java.lang.Object.class, (PageContext)_jspx_page_context, null, false));
    // /WEB-INF/jsp/view-category-include.jsp(24,2) name = var type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fforEach_005f5.setVar("category");
    // /WEB-INF/jsp/view-category-include.jsp(24,2) name = varStatus type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fforEach_005f5.setVarStatus("status");
    int[] _jspx_push_body_count_c_005fforEach_005f5 = new int[] { 0 };
    try {
      int _jspx_eval_c_005fforEach_005f5 = _jspx_th_c_005fforEach_005f5.doStartTag();
      if (_jspx_eval_c_005fforEach_005f5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        do {
          out.write("\r\n");
          out.write("\t\t\t");
          if (_jspx_meth_c_005fif_005f21(_jspx_th_c_005fforEach_005f5, _jspx_page_context, _jspx_push_body_count_c_005fforEach_005f5))
            return true;
          if (_jspx_meth_jamwiki_005flink_005f15(_jspx_th_c_005fforEach_005f5, _jspx_page_context, _jspx_push_body_count_c_005fforEach_005f5))
            return true;
          out.write("\r\n");
          out.write("\t\t");
          int evalDoAfterBody = _jspx_th_c_005fforEach_005f5.doAfterBody();
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
      }
      if (_jspx_th_c_005fforEach_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        return true;
      }
    } catch (Throwable _jspx_exception) {
      while (_jspx_push_body_count_c_005fforEach_005f5[0]-- > 0)
        out = _jspx_page_context.popBody();
      _jspx_th_c_005fforEach_005f5.doCatch(_jspx_exception);
    } finally {
      _jspx_th_c_005fforEach_005f5.doFinally();
      _005fjspx_005ftagPool_005fc_005fforEach_0026_005fvarStatus_005fvar_005fitems.reuse(_jspx_th_c_005fforEach_005f5);
    }
    return false;
  }

  private boolean _jspx_meth_c_005fif_005f21(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fforEach_005f5, PageContext _jspx_page_context, int[] _jspx_push_body_count_c_005fforEach_005f5)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:if
    org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f21 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
    _jspx_th_c_005fif_005f21.setPageContext(_jspx_page_context);
    _jspx_th_c_005fif_005f21.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fforEach_005f5);
    // /WEB-INF/jsp/view-category-include.jsp(25,3) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fif_005f21.setTest(((java.lang.Boolean) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${!status.first}", java.lang.Boolean.class, (PageContext)_jspx_page_context, null, false)).booleanValue());
    int _jspx_eval_c_005fif_005f21 = _jspx_th_c_005fif_005f21.doStartTag();
    if (_jspx_eval_c_005fif_005f21 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("&#160;|&#160;");
        int evalDoAfterBody = _jspx_th_c_005fif_005f21.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_c_005fif_005f21.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f21);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f21);
    return false;
  }

  private boolean _jspx_meth_jamwiki_005flink_005f15(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fforEach_005f5, PageContext _jspx_page_context, int[] _jspx_push_body_count_c_005fforEach_005f5)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  jamwiki:link
    org.jamwiki.taglib.LinkTag _jspx_th_jamwiki_005flink_005f15 = (org.jamwiki.taglib.LinkTag) _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvalue_005ftext_005fnobody.get(org.jamwiki.taglib.LinkTag.class);
    _jspx_th_jamwiki_005flink_005f15.setPageContext(_jspx_page_context);
    _jspx_th_jamwiki_005flink_005f15.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fforEach_005f5);
    // /WEB-INF/jsp/view-category-include.jsp(25,53) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005flink_005f15.setValue((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${category.key}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
    // /WEB-INF/jsp/view-category-include.jsp(25,53) name = text type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_jamwiki_005flink_005f15.setText((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${category.value}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
    int _jspx_eval_jamwiki_005flink_005f15 = _jspx_th_jamwiki_005flink_005f15.doStartTag();
    if (_jspx_th_jamwiki_005flink_005f15.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvalue_005ftext_005fnobody.reuse(_jspx_th_jamwiki_005flink_005f15);
      return true;
    }
    _005fjspx_005ftagPool_005fjamwiki_005flink_0026_005fvalue_005ftext_005fnobody.reuse(_jspx_th_jamwiki_005flink_005f15);
    return false;
  }
}
