package org.swing.sample.stocks;

/**
 * @author Vlad Vinichenko (akerigan@gmail.com)
 * @since 02.10.2010 0:06:12 (Europe/Moscow)
 */
public class StockData {

    public String m_symbol;

    public String m_name;

    public Double m_last;

    public Double m_open;

    public Double m_change;

    public Double m_changePr;

    public Long m_volume;

    public StockData(String symbol, String name, double last,

                     double open, double change, double changePr, long volume) {

        m_symbol = symbol;

        m_name = name;

        m_last = new Double(last);

        m_open = new Double(open);

        m_change = new Double(change);

        m_changePr = new Double(changePr);

        m_volume = new Long(volume);

    }


}
