package org.clh.jsfv.file;

class StateFileNames {

    static String UNKNOWN(Integer checkedCount, Integer totaltCount) {
        return createStateFileName(DirectoryHeaderfile.SITE_NAME, "UNKNOWN", checkedCount, totaltCount);
    }

    static String OK(Integer checkedCount, Integer totaltCount) {
        return createStateFileName(DirectoryHeaderfile.SITE_NAME, "OK", checkedCount, totaltCount);
    }

    static String INCOMPLETE(Integer checkedCount, Integer totaltCount) {
        return createStateFileName(DirectoryHeaderfile.SITE_NAME, "INCOMPLETE", checkedCount, totaltCount);
    }

    private static String createStateFileName(String site, String state, int okCount, int totaltCount) {
        return String.format("-[%s]_%s_%d%%_%d_OF_%d_FILES_[%s]-", site, state, percent(okCount, totaltCount), okCount, totaltCount, site);
    }

    private static int percent(int count, double total) {
        return (int) ((count / total )*100);
    }


}
