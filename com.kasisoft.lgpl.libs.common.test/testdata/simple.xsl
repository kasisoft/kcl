<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:output method="text" encoding="ISO-8859-1"/>
  
  <xsl:template match="/">
    <xsl:apply-templates select="bookstore"/>
  </xsl:template>

  <xsl:template match="bookstore">
    <xsl:apply-templates select="title"/>
  </xsl:template>

  <xsl:template match="title">
    <xsl:value-of select="text()"/>
  </xsl:template>
  
</xsl:stylesheet> 