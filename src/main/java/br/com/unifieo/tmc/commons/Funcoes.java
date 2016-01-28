package br.com.unifieo.tmc.commons;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.Predicate;
import org.omg.CORBA.Environment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.MonthDay;
import java.time.Year;
import java.time.YearMonth;
import java.util.*;

/**
 * Classe de uso genérico com funções reutilizaveis para qualquer sistema.
 * <p>
 * <b>Evite ao máximo escrever métodos que tenham dependências alem da api</b>
 *
 * @since 12/09/2011
 */
public class Funcoes {

    /**
     * Preposições comuns da lingua portuguesa.
     */
    static final String[] PREPOSICOES = {"a", "ao", "à", "aos", "às", "de", "do", "da", "dos", "das", "em", "no", "na", "nos", "nas", "por", "pelo", "pela", "pelos", "pelas", "ante", "após", "até", "com", "contra", "desde", "entre", "para", "perante", "sem", "sob", "sobre", "trás"};

    /**
     * Método para geração de uma String de Log.
     * Exemplo: Fulano - 12/09/2011 - 22:31:42
     *
     * @param nomeUsuario
     * @return Uma string que pode ser usada como Log para qualquer mudança no
     * sitema.
     */
    public static String getCampoLog(String nomeUsuario) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
        return nomeUsuario.concat(" - ").concat(format.format(new Date()));
    }

    /**
     * Método para obter uma determinada data afim de ser utilizada em uma query sql.
     *
     * @param AData Data desejada instanciada da classe Date.
     * @return A data do dia corrente para ser usada em Querys SQL.
     * @throws ParseException
     */
    public static Date getDataSQL(Date AData, String formato) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(formato);
        Date dDataAtual = format.parse(format.format(AData));
        java.sql.Date dataSQL = new java.sql.Date(dDataAtual.getTime());
        return dataSQL;
    }

    /**
     * Para obter uma descrição de um determinadado período.
     *
     * @param dataInicial
     * @param dataFinal
     * @return Ex: "De 01/05/2013 à 31/05/2013"
     * @since 25/06/2013
     */
    public static String getDescricaoPeriodo(Date dataInicial, Date dataFinal) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String inicio = format.format(dataInicial);
        String fim = format.format(dataFinal);
        return "De ".concat(inicio).concat(" à ").concat(fim);
    }

    /**
     * Obtem uma descrição da data por extenso com dia da semana, mês e ano.
     *
     * @param data
     * @return
     */
    public static String getDataPorExtenso(Date data) {
        DateFormat dfmt = new SimpleDateFormat("EEEE, d 'de' MMMM 'de' yyyy");
        return dfmt.format(data);
    }

    /**
     * Obtem a data atual por extenso.
     *
     * @return
     */
    public static String getDataPorExtenso() {
        return Funcoes.getDataPorExtenso(new Date());
    }

    /**
     * Para obter a hora de uma determinada data.
     *
     * @param AData
     * @return
     */
    public static String getHora(Date AData) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        String sHoraAtual = format.format(AData);
        return sHoraAtual;
    }

    /**
     * Para obter a hora atual.
     *
     * @return
     */
    public static String getHoraAtual() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        String sHoraAtual = format.format(new Date());
        return sHoraAtual;
    }

    /**
     * Auxiliar ao carregamento de um arquivo por Stream.
     *
     * @param resource
     * @return Stream de um determinado arquivo.
     */
    public static InputStream getInputStream(String resource) {
        String stripped = resource.startsWith("/") ? resource.substring(1) : resource;
        InputStream stream = null;
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader != null)
            stream = classLoader.getResourceAsStream(stripped);
        if (stream == null)
            stream = Environment.class.getResourceAsStream(resource);
        if (stream == null)
            stream = Environment.class.getClassLoader().getResourceAsStream(stripped);
        if (stream == null)
            throw new RuntimeException(resource.concat(" não encontrado"));
        return stream;
    }

    /**
     * Para obter uma data sem a descrição de horário.
     *
     * @param date
     * @return ddMMyyyy 00:00:00
     */
    public static Date getOnlyDate(Date date) {
        Date result = null;
        if (date != null) {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            try {
                String temp = format.format(date);
                result = format.parse(temp);
            } catch (ParseException e) {
                throw new RuntimeException("Problemas para remover o horário da Data: " + date);
            }
        }
        return result;
    }

    /**
     * Para obter a data atual sem a descrição de horário.
     *
     * @return ddMMyyyy 00:00:00
     */
    public static Date getOnlyDate() {
        return Funcoes.getOnlyDate(new Date());
    }

    /**
     * Obtem a data atual no formato aaaaMMdd.
     *
     * @return Data atual, ex: 20160128 (28/01/2016)
     */
    public static int getIntDate() {
        String year = String.valueOf(Year.now().getValue());
        String month = String.valueOf(YearMonth.now().getMonthValue());
        if (Integer.parseInt(month) < 10)
            month = "0".concat(month);
        String day = String.valueOf(MonthDay.now().getDayOfMonth());
        if (Integer.parseInt(day) < 10)
            day = "0".concat(day);
        String date = year.concat(month).concat(day);
        return Integer.parseInt(date);
    }

    /**
     * Reescreve um determinado valor monetário com zeros a esquerda para
     * correta formatação.
     *
     * @param valor   Valor a ser formatado.
     * @param digitos Quantidade de digitos a esquerda.
     * @return Novo valor formatado.
     */
    public static String getZerosAEsquerda(final String valor, final int digitos) {
        StringBuilder builder = new StringBuilder(digitos);
        int length = (digitos - valor.length());
        for (int iCount = 0; iCount < length; iCount++)
            builder.append("0");
        builder.append(valor);
        return builder.toString();
    }

    /**
     * Verifica se um determinado valor existe, impede um saudoso 'NullPointerException'
     *
     * @param value
     * @return true or false
     */
    public static boolean isExists(String value) {
        return (value != null && !value.isEmpty());
    }

    /**
     * Retorna uma expressão regular com base nas preposições.
     *
     * @return Expressão regular
     */
    static String getRegexPreposicoes() {
        final int length = PREPOSICOES.length;
        StringBuilder builder = new StringBuilder(length * 2);
        for (byte count = 0; count < length; count++) {
            if (count == 0)
                builder.append("[");
            if (count > 0)
                builder.append("-");
            builder.append(PREPOSICOES[count]);
            if ((count + 1) == length)
                builder.append("]");
        }
        return builder.toString();
    }

    /**
     * Remove as preposições de uma determinada frase.
     *
     * @param referencia
     * @return frase limpa
     */
    public static String doRemovePreposicao(final String referencia) {
        LinkedList<String> referList = new LinkedList<String>(Arrays.asList(referencia.split(" ")));
        CollectionUtils.filter(referList, new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                boolean result = false;
                for (String item : PREPOSICOES) {
                    result = ((String) object).equalsIgnoreCase(item);
                    if (result) break;
                }
                return !result;
            }
        });
        StringBuilder builder = new StringBuilder(referencia.length());
        for (String item : referList)
            builder.append(item).append(" ");
        return builder.toString().trim();
    }

    /**
     * Remove caracteres de uma String como traços, underscore, pontos e virgulas,
     * deixando apenas letras de A-Z e números de 0-9.
     * <p>
     * A expressão regular [^0-9A-Za-z] identifica tudo aquilo que
     * não for um caracter alfanumérico.
     *
     * @param value
     * @return String limpa.
     */
    public static String doRemoveCaracteres(String value) {
        String newValue = value.replaceAll("[^0-9A-Za-z]", "").trim();
        return newValue;
    }

    /**
     * Auxiliar para trucamento de valores String.
     *
     * @param value Valor a ser truncado.
     * @param init  Index do caracter inicial.
     * @param end   Index do caracter final.
     * @return
     */
    private static String getTrucate(final String value, final int init, final int end) {
        if (value == null) return new String();
        String result = value;
        if (result.length() >= end)
            result = value.substring(init, end);
        return result;
    }

    /**
     * Truca, da direita para a esquerda, uma determinada String
     * retornando um valor menor em relação ao seu real tamanho.
     *
     * @param value
     * @param length
     * @return
     */
    public static String getTruncateRigthToLeft(final String value, final int length) {
        return Funcoes.getTrucate(value, 0, length);
    }

    /**
     * Truca, da esquerda para a direita, uma determinada String
     * retornando um valor menor em relação ao seu real tamanho.
     *
     * @param value
     * @param length
     * @return
     */
    public static String getTruncateLeftToRigth(final String value, final int length) {
        int init = value.length() - length;
        if (init < 0) init = init * -1;
        return Funcoes.getTrucate(value, init, value.length());
    }

    /**
     * Imprime o conteudo de uma coleção de Strings pulando uma linha a
     * cada conjunto de caracteres, fomando uma lista vertical.
     *
     * @param lista
     * @return
     */
    public static String doPrintListVertical(Collection<String> lista) {
        StringBuilder result = new StringBuilder(lista.size() * 2);
        for (String item : lista) {
            result.append(item);
            result.append("\n");
        }
        return result.toString();
    }

    /**
     * Obtem o endereço IP da internet.
     *
     * @return
     */
    public static String getIPExterno() {
        URL whatismyip = null;
        try {
            whatismyip = new URL("http://checkip.amazonaws.com");
        } catch (MalformedURLException e) {
            // NoCommand
        }
        return Funcoes.getHttpContent(whatismyip);
    }

    /**
     * Obtem o conteudo de uma página Web com base na sua URL.
     *
     * @param url
     * @return
     */
    public static String getHttpContent(URL url) {
        String content = new String();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            content = in.readLine();
            in.close();
        } catch (IOException e) {
            // NoCommand
        }
        return content;
    }

    /**
     * Obtem um mapa de valores comumente utilizado por frameworks como parametros.
     * <p>
     * Exemplos da utilização desse tipo de mapa pode ser visto no Hibernate,
     * na pesquisa utilizando NamedQuerys e no JasperReports, onde um mapa
     * de valores é utilizado para exibição de valores no relatório
     * e execução de alguma query interna.
     *
     * @param array Ex: Object[][] array = { {"chave1", new Object()}, {"chave2", new Object()} };
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> getParametros(Object[] array) {
        return MapUtils.putAll(new HashMap<K, V>(), array);
    }

    /**
     * Converte um Date em uma String no formato dd/MM/yyyy.
     *
     * @param dataString
     * @return strin formatada de uma data.
     * @throws ParseException
     */
    public static Date getDateBy(String dataString) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.parse(dataString);
    }

    /**
     * Converte um número para o número de casas decimais indicada.
     *
     * @param number
     * @param casasDecimais
     * @return Número arredondo com as casas decimais definidas
     */
    public static Double RoundTo(Double number, int casasDecimais) {
        if (casasDecimais < 0)
            throw new ArithmeticException("As casas decimais deve ser igual ou superior a 0 (zero).");

        Double numberDivider = Math.pow(10, casasDecimais);
        Double resultNumber = number * numberDivider;

        return Math.round(resultNumber) / numberDivider;
    }
}
