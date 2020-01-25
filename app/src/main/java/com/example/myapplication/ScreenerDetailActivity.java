package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ScreenerDetailActivity extends BaseActivity{
    public DrawerLayout drawer;
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mNamesFull = new ArrayList<>();
    StocklistRecyclerViewAdapter adapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "BaseActivity";

    private static final String KEY_SCREENER = "detection";
    //    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        initStockName();
    }

//    private void initStockName(){
//        Log.d(TAG, "initStockName: Populating stock list");
//        mNames.addAll(Arrays.asList("3A", "AASIA", "AAX", "ABLEGRP", "ABMB", "ACME", "ACOSTEC", "ADVCON", "ADVENTA", "ADVPKG", "AEM", "AEMULUS", "AEON", "AEONCR", "AFFIN", "AFUJIYA", "AHB", "AHEALTH", "AHP", "AIM", "AIRASIA", "AIRPORT", "AISB", "AJI", "AJIYA", "ALAM", "ALAQAR", "ALCOM", "ALLIANZ", "ALSREIT", "AMBANK", "AME", "AMEDIA", "AMFIRST", "AMPROP", "AMTEK", "AMTEL", "AMVERTON", "AMWAY", "ANALABS", "ANCOM", "ANCOMLB", "ANNJOO", "ANZO", "APB", "APEX", "APFT", "APM", "APOLLO", "APPASIA", "ARANK", "ARBB", "ARMADA", "ARREIT", "ASB", "ASDION", "ASIABRN", "ASIAFLE", "ASIAPAC", "ASIAPLY", "ASTINO", "ASTRO", "AT", "ATAIMS", "ATLAN", "ATRIUM", "ATTA", "AVI", "AWC", "AXIATA", "AXREIT", "AYER", "AYS", "AZRB", "BAHVEST", "BARAKAH", "BAT", "BAUTO", "BCB", "BCMALL", "BDB", "BENALEC", "BERTAM", "BHIC", "BIG", "BIMB", "BINACOM", "BINTAI", "BIOHLDG", "BIPORT", "BJASSET", "BJCORP", "BJFOOD", "BJLAND", "BJMEDIA", "BJTOTO", "BKAWAN", "BLDPLNT", "BOILERM", "BONIA", "BORNOIL", "BOXPAK", "BPLANT", "BPPLAS", "BPURI", "BRAHIMS", "BREM", "BRIGHT", "BSLCORP", "BSTEAD", "BTECH", "BTM", "BURSA", "CAB", "CABNET", "CAELY", "CAMRES", "CANONE", "CAP", "CAREPLS", "CARIMIN", "CARING", "CARLSBG", "CBIP", "CCB", "CCK", "CCM", "CENSOF", "CEPAT", "CEPCO", "CFM", "CHEETAH", "CHHB", "CHINHIN", "CHINTEK", "CHINWEL", "CHOOBEE", "CHUAN", "CIHLDG", "CIMB", "CJCEN", "CLIQ", "CME", "CMMT", "CMSB", "CNASIA", "CNI", "CNOUHUA", "COASTAL", "COCOLND", "COMCORP", "COMFORT", "COMPLET", "COMPUGT", "CONNECT", "CRESBLD", "CRESNDO", "CSCENIC", "CSCSTEL", "CSL", "CUSCAPI", "CVIEW", "CWG", "CYL", "CYMAO", "CYPARK", "DAIBOCI", "DAIMAN", "DANCO", "DATAPRP", "DAYA", "DAYANG", "DBE", "DBHD", "DEGEM", "DELEUM", "DESTINI", "DGB", "DGSB", "DIALOG", "DIGI", "DIGISTA", "DKLS", "DKSH", "DLADY", "DNEX", "DNONCE", "DOLMITE", "DOLPHIN", "DOMINAN", "DPHARMA", "DPIH", "DPS", "DRBHCOM", "DSONIC", "DUFU", "DUTALND", "DWL", "DYNACIA", "EAH", "EASTLND", "EATECH", "ECM", "ECOFIRS", "ECOHLDS", "ECONBHD", "ECOWLD", "EDARAN", "EDEN", "EDGENTA", "EDUSPEC", "EFFICEN", "EFORCE", "EG", "EIG", "EITA", "EKA", "EKOVEST", "EKSONS", "ELKDESA", "ELSOFT", "EMETALL", "EMICO", "ENCORP", "ENGKAH", "ENGTEX", "ENRA", "EPMB", "ESAFE", "ESCERAM", "EUPE", "EURO", "EUROSP", "EVERGRN", "EWEIN", "EWINT", "FACBIND", "FAJAR", "FAREAST", "FARLIM", "FAVCO", "FCW", "FGV", "FIAMMA", "FIBON", "FIHB", "FIMACOR", "FINTEC", "FITTERS", "FLBHD", "FOCUS", "FOCUSP", "FPGROUP", "FPI", "FREIGHT", "FRONTKN", "FSBM", "G3", "GADANG", "GAMUDA", "GASMSIA", "GBGAQRS", "GCB", "GCE", "GDB", "GDEX", "GENETEC", "GENM", "GENP", "GENTING", "GESHEN", "GETS", "GFM", "GHLSYS", "GKENT", "GLBHD", "GLOMAC", "GLOTEC", "GMUTUAL", "GNB", "GOB", "GOCEAN", "GOODWAY", "GOPENG", "GPA", "GPACKET", "GPHAROS", "GRANFLO", "GREENYB", "GSB", "GTRONIC", "GUH", "GUNUNG", "GUOCO", "HAIO", "HANDAL", "HAPSENG", "HARBOUR", "HARISON", "HARNLEN", "HARTA", "HBGLOB", "HCK", "HEIM", "HEKTAR", "HENGYUAN", "HEVEA", "HEXZA", "HHGROUP", "HHHCORP", "HIAPTEK", "HIBISCS", "HIGHTEC", "HIL", "HLBANK", "HLCAP", "HLFG", "HLIND", "HLT", "HOHUP", "HOKHENG", "HOMERIZ", "HOOVER", "HSL", "HSPLANT", "HSSEB", "HTPADU", "HUAAN", "HUAYANG", "HUBLINE", "HUMEIND", "HUPSENG", "HWATAI", "HWGB", "IBHD", "IBRACO", "ICAP", "ICON", "IDEAL", "IDMENSN", "IFCAMSC", "IGBB", "IGBREIT", "IHH", "IJM", "IJMPLNT", "IKHMAS", "ILB", "IMASPRO", "IMPIANA", "INARI", "INCKEN", "INIX", "INNITY", "INNO", "INSAS", "INTA", "IOICORP", "IOIPG", "IPMUDA", "IQGROUP", "IREKA", "IRETEX", "IRIS", "ITRONIC", "IVORY", "IWCITY", "JADI", "JAG", "JAKS", "JASKITA", "JAYCORP", "JCBNEXT", "JCY", "JERASIA", "JETSON", "JFTECH", "JHM", "JIANKUN", "JKGLAND", "JMR", "JOHAN", "JOHOTIN", "JTIASA", "K1", "KAB", "KAMDAR", "KANGER", "KAREX", "KARYON", "KAWAN", "KBUNAI", "KEINHIN", "KEN", "KENANGA", "KERJAYA", "KESM", "KEYASIC", "KFIMA", "KFM", "KGB", "KGROUP", "KHEESAN", "KHIND", "KIALIM", "KIANJOO", "KIMHIN", "KIMLUN", "KINSTEL", "KIPREIT", "KKB", "KLCC", "KLK", "KLUANG", "KMLOONG", "KNM", "KNUSFOR", "KOBAY", "KOMARK", "KOSSAN", "KOTRA", "KPJ", "KPOWER", "KPS", "KPSCB", "KRETAM", "KRONO", "KSENG", "KSL", "KSSC", "KSTAR", "KTB", "KTC", "KUB", "KUCHAI", "KWANTAS", "KYM", "LAFMSIA", "LAMBO", "LANDMRK", "LATITUD", "LAYHONG", "LBALUM", "LBICAP", "LBS", "LCTITAN", "LEBTECH", "LEESK", "LEONFB", "LEWEKO", "LFECORP", "LIENHOE", "LIIHEN", "LIONFIB", "LIONIND", "LITRAK", "LKL", "LONBISC", "LPI", "LSTEEL", "LTKM", "LUSTER", "LUXCHEM", "LYC", "LYSAGHT", "M3TECH", "MAA", "MAGNA", "MAGNI", "MAGNUM", "MAHSING", "MALAKOF", "MALPAC", "MALTON", "MANULFE", "MARCO", "MASTEEL", "MASTER", "MATANG", "MATRIX", "MAXIS", "MAXWELL", "MAYBANK", "MAYBULK", "MBG", "MBL", "MBMR", "MBSB", "MBWORLD", "MCEHLDG", "MCLEAN", "MCT", "MEDIA", "MEDIAC", "MEGASUN", "MELATI", "MELEWAR", "MENANG", "MENTIGA", "MERCURY", "MERGE", "MESB", "METROD", "MFCB", "MFLOUR", "MGB", "MGRC", "MHB", "MHC", "MI", "MICROLN", "MIECO", "MIKROMB", "MILUX", "MINDA", "MINETEC", "MINHO", "MISC", "MITRA", "MJPERAK", "MKH", "MKLAND", "MLAB", "MMAG", "MMCCORP", "MMSV", "MNC", "MNRB", "MPAY", "MPCORP", "MPHBCAP", "MPI", "MQREIT", "MQTECH", "MRCB", "MSC", "MSM", "MSNIAGA", "MSPORTS", "MTDACPI", "MTOUCHE", "MTRONIC", "MUDA", "MUDAJYA", "MUH", "MUHIBAH", "MUIIND", "MUIPROP", "MULPHA", "MYCRON", "MYEG", "MYNEWS", "N2N", "NADIBHD", "NAIM", "NATWIDE", "NESTLE", "NESTLE", "NETX", "NEXGRAM", "NGGB", "NHFATT", "NICE", "NIHSIN", "NOTION", "NOVA", "NOVAMSC", "NPC", "NSOP", "NTPM", "NWP", "NYLEX", "OCB", "OCK", "OCNCASH", "OCR", "OFI", "OIB", "OKA", "OLYMPIA", "OMESTI", "OPCOM", "OPENSYS", "ORIENT", "ORION", "ORNA", "OSK", "OSKVI", "OVERSEA", "OWG", "PA", "PADINI", "PANAMY", "PANSAR", "PANTECH", "PAOS", "PARAGON", "PARAMON", "PARKSON", "PARLO", "PASDEC", "PASUKGB", "PAVREIT", "PBA", "PBBANK", "PCCS", "PCHEM", "PDZ", "PEB", "PECCA", "PELIKAN", "PENERGY", "PENSONI", "PENTA", "PERDANA", "PERISAI", "PERMAJU", "PERSTIM", "PESONA", "PESTECH", "PETDAG", "PETGAS", "PETRONM", "PGLOBE", "PHARMA", "PHB", "PICORP", "PIE", "PINEAPP", "PINEPAC", "PJBUMI", "PLABS", "PLB", "PLENITU", "PLS", "PMBTECH", "PMCORP", "PMETAL", "PMHLDG", "PNEPCB", "POHKONG", "POHUAT", "POLY", "POS", "PPB", "PPG", "PPHB", "PRESBHD", "PRESTAR", "PRG", "PRIVA", "PRKCORP", "PRLEXUS", "PRTASCO", "PSIPTEK", "PTARAS", "PTB", "PTRANS", "PUC", "PUNCAK", "PWF", "PWORTH", "PWROOT", "QES", "QL", "QUALITY", "RALCO", "RANHILL", "RAPID", "RCECAP", "REACH", "REDTONE", "RESINTC", "REV", "REVENUE", "REX", "REXIT", "RGB", "RGTECH", "RHBBANK", "RHONEMA", "ROHAS", "RSAWIT", "RSENA", "RUBEREX", "RVIEW", "SAB", "SALCON", "SALUTE", "SAM", "SAMCHEM", "SANBUMI", "SANICHI", "SAPIND", "SAPNRG", "SAPRES", "SASBADI", "SAUDEE", "SBAGAN", "SBCCORP", "SCABLE", "SCBUILD", "SCC", "SCGM", "SCH", "SCIB", "SCICOM", "SCIENTX", "SCNWOLF", "SCOMI", "SCOMIES", "SCOMNET", "SCOPE", "SDRED", "SEACERA", "SEAL", "SEALINK", "SEB", "SEDANIA", "SEEHUP", "SEG", "SEM", "SENDAI", "SERBADK", "SERNKOU", "SERSOL", "SHANG", "SHCHAN", "SHH", "SHL", "SIGGAS", "SIGN", "SIME", "SIMEPLT", "SIMEPROP", "SINOTOP", "SJC", "SKBSHUT", "SKPRES", "SLP", "SMCAP", "SMETRIC", "SMI", "SMISCOR", "SMRT", "SMTRACK", "SNC", "SNTORIA", "SOLID", "SOLUTN", "SONA", "SOP", "SPB", "SPRITZER", "SPSETIA", "SRIDGE", "SSTEEL", "STAR", "STONE", "STRAITS", "SUBUR", "SUCCESS", "SUIWAH", "SUMATEC", "SUNCON", "SUNREIT", "SUNSURIA", "SUNWAY", "SUNZEN", "SUPERLN", "SUPERMX", "SURIA", "SWKPLNT", "SWSCAP", "SYCAL", "SYF", "SYMLIFE", "SYSCORP", "SYSTECH", "T7GLOBAL", "TA", "TAANN", "TADMAX", "TAFI", "TAGB", "TAKAFUL", "TALAMT", "TALIWRK", "TAMBUN", "TANCO", "TAS", "TASCO", "TASEK", "TAWIN", "TCHONG", "TDEX", "TDM", "TECFAST", "TECGUAN", "TECHBND", "TEKSENG", "TENAGA", "TEOSENG", "TEXCHEM", "TEXCYCL", "TFP", "TGL", "TGUAN", "THETA", "THHEAVY", "THPLANT", "THRIVEN", "TIENWAH", "TIGER", "TIMECOM", "TIMWELL", "TITIJYA", "TM", "TMCLIFE", "TNLOGIS", "TOCEAN", "TOMEI", "TOMYPAK", "TONGHER", "TOPGLOV", "TOYOINK", "TPC", "TRC", "TRIMODE", "TRIVE", "TROP", "TSH", "TSRCAP", "TUNEPRO", "TURBO", "TURIYA", "TWRREIT", "UCHITEC", "UCREST", "UEMS", "ULICORP", "UMCCA", "UMS", "UMSNGB", "UMW", "UNIMECH", "UNISEM", "UOADEV", "UOAREIT", "UPA", "UTDPLT", "UTUSAN", "UZMA", "VC", "VELESTO", "VERSATL", "VERTICE", "VIS", "VITROX", "VIVOCOM", "VIZIONE", "VS", "VSOLAR", "WANGZNG", "WARISAN", "WASEONG", "WATTA", "WCT", "WEGMANS", "WELLCAL", "WIDAD", "WIDETEC", "WILLOW", "WINTONI", "WMG", "WONG", "WOODLAN", "WPRTS", "WTHORSE", "WTK", "WZSATU", "XDL", "XIANLNG", "XINGHE", "XINHWA", "XINQUAN", "XOX", "YBS", "YEELEE", "YFG", "YGL", "YILAI", "YINSON", "YKGI", "YLI", "YNHPROP", "YOCB", "YONGTAI", "YSPSAH", "YTL", "YTLLAND", "YTLPOWR", "YTLREIT", "ZECON", "ZELAN", "ZHULIAN")
//        );
//        mNamesFull.addAll(mNames);
//
//        initRecyclerView();
//    }

    private void initStockName() {

        String screener_name = getIntent().getStringExtra("Screener");
        Log.d("screener name", screener_name);
        db.collection("screeners").document(screener_name).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

//                        ArrayList <String> stocklist = new ArrayList<>();
                        DocumentSnapshot document = task.getResult();
                        List<String> stocklist = (List<String>) document.get(KEY_SCREENER);
//                        String stockstring = document.getData().toString();
//                        String[] stocklist = stockstring.split(", ");

                        Log.d("myTag", stocklist.get(0));

                        mNames.addAll((stocklist));
                        mNamesFull.addAll(mNames);

                        initRecyclerView();
                        }
                    }

                )

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
        initRecyclerView();
    }






    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerview");
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        adapter = new StocklistRecyclerViewAdapter(this, mNames, mNamesFull);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }





}




