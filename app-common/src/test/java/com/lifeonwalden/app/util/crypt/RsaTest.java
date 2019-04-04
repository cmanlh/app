/*
 *    Copyright 2019 CManLH
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.lifeonwalden.app.util.crypt;

import com.lifeonwalden.app.util.crypt.bean.KeyPairBean;
import org.junit.Assert;
import org.junit.Test;

public class RsaTest {
    @Test
    public void serverSite() {
        KeyPairBean keyPairBean = Rsa.generateKeyWithEncode(2048);
        System.out.println("Private Key : ".concat(keyPairBean.getPrivateKey()));
        System.out.println();
        System.out.println("Public Key : ".concat(keyPairBean.getPublicKey()));
        System.out.println();

        String originalText = "加解密字符串hello world!逆向工程";
        String encryptedText = Rsa.encrypt(keyPairBean.getPublicKey(), originalText);
        System.out.println("Encrypted text: ".concat(encryptedText));
        System.out.println();

        String decryptedText = Rsa.decrypt(keyPairBean.getPrivateKey(), encryptedText);
        System.out.println(decryptedText);
        System.out.println();

        Assert.assertTrue(originalText.equals(decryptedText));
    }

    @Test
    public void decryptWithClientKey() {
        String originalText = "加解密字符串hello world!逆向工程";
        String privateKey = "=8Sggw=wAQAC=qkgBN=3bISGC=BEQAN=CSAAF=CCjpE=BIgoE=BIo=PMNAB=nxXGrB=xIeaq=V7dgBB=6DBkJC=+htCDD=1g456=pDPqgC=mhukb=41rco=pxtqJD=tJRtsC=+KKkv=jLMuo=zYp14D=BY96YD=KcNHlC=+WL2W=bqyyJ=oWx/t=bsGojC=wUielC=Uhxos=zBgpgC=x7GPxB=/4OJEB=JYhsL=/l8MTD=bbkNHB=9W00b=maPx7C=Se0A5=erNkzD=DkAFhD=LN17XD=O9mCRD=LHUYYC=rBSjnD=C7tl3=qZBrTB=kyc19=YH1TND=Bwyc5B=DseOo=RtdRPD=PW4v4=E0v2ZD=jwy5y=PzqfHD=1eewwB=eVmkBC=gBlZeC=MufjIB=NP8FgC=xzQYnB=rOqjhC=Y+CveD=nem5HB=TQXoAB=OMZRKD=BQ2GR=lAI/R=Dwg0CD=DdjmuB=DIQb0C=CEAAB=MAQACC=GzIpwC=by0e8D=jaFw8D=ekusT=9yBPYB=Y44EpB=x7FZpB=LpAjYB=n1fWe=fO90zB=+YbMx=qNGzkB=nhlOMD=ySPEMC=mJzFvC=l2E5u=d/ySxC=FEZOiC=JagXfB=+9Ww3C=Ca8IZB=UJTEmB=SJRweD=BPYyR=+WQkb=JiyZsB=cMd28=l1koLC=5pFvjB=xyPlFC=tvbd5D=TAwVUD=SbQ/sD=nRxvmC=Xgh+5=nz2EdD=mZCOpC=/QrQeC=ODu2PB=nPWTq=Q/B8+B=uQ0ev=bHPu+=r80L8B=qG2puB=+8fgJ=rhZpnC=5K9At=YlPGsB=365sY=rzciYC=mkzdeB=zk2gHD=HKtOPC=IAooL=mv+MrB=Efv/kD=8K0Nw=uXyKO=Q85pZ=NFmpIB=rQF9wB=RpaqLD=CkCjj=wDQgBC=PdkJxB=lmJpOD=es7HEB=ILSB7=nzBFdC=5zAN0C=sVlss=cCV5wD=Q2pGN=9Flde=kQdkXD=R/E3xD=5yLbAC=EHmssD=WVKozB=9OCNY=NV0c0D=i2CZCC=RsL97=T8xZ7C=ZcmIWB=l/GVoB=1wRJjD=ElIsg=IjyjwC=46YPl=tcH6YD=VHh/jD=Ban94=YkAL8C=9XsDMD=CUdk2C=gDQgBC=6FZk3C=WHmRBB=Hiu+=mOC9WC=iW+S+D=eOcfh=GvuSfC=qMgFDD=fnYSN=V1ZQdC=r+i25C=I5dKED=8UdYIB=Yq6HYC=VDHavD=ZusbfB=j4Mwl=HSyZAB=vg5IR=2D/2zD=Pw+Bm=61cfS=6ePDFB=iMm0vC=BsmF4C=1ZU8lC=/CrYo=DcagSC=6jA7jB=dg3w6D=EOeffD=CkDTK=vGBgBC=MveJxD=/6btC=mGgKNC=6wW0LB=2pT4JC=u4fI6=wo+i4B=sycXqC=hMUG2D=eIGiKD=cWsf6B=HIPH5D=giFelB=O1k29D=sFgMIB=2CZYUB=UxByrC=sMANMC=5b56v=DU6MXB=/9lEAC=FUwPa=fKKl5=NsK/dD=XUh6lD=FCTOQ=XA/ScD=c9FeGB=btVXK=CYXTRD=DH7zEC=BKQZ9=3Q8PAC=iiah9B=2nwP1=xi9Jp=2VNsc=gDjCqB=W3uMED=24wwPD=63P3fD=oAoMBB=jUjYPB=8bR5xD=tpHhe=9ocmwC=A3sJq=Rw9SgD=5ynmPD=PRbhS=vdLcQD=uwzTXB=A0LWMD=qJ2f=FjOdqD=D6uQ+=ukB+PC=fNJXbD=q/B98=GNyZpC=GgtQBD=p6WoxD=Ac6Gr=gn/HND=AGoAZ=dX4V7=dV+s6C=NGk0yC=qIzx1B=aDMbTD=47ynGB=We5oo=+6xwHD=KJ1adC=mFzenD=IllwO=o589jD=C/gY3B=+YqON=dbvl7C=5HfJkD=eij1yB=QW4J7D=8kfsSD=fEeEhD=LrI7tB=I9eYbB=E208iC=w370DD=gQnjT=5wyAO=Jnk+c=8RmOVD=t8ptoD=y3x5yC=sIYIyC=yU8CM";
        String encryptedText = "=xCMAEB=EAnFsD=1zD3SC=LNxfFC=/+zSM=aSFTDC=CIVyyD=Vb0Md=gd9DXC=v1EKl=6TRoNB=reL9/B=3L4iy=bw+t/=T+sylB=zIEwbC=KXU8PB=8DsZaB=wq/cIB=ISfxtD=Xf5JlD=VXUg6B=F+YkAD=/ziFsC=6PsO=B9xHnD=Ihjro=mSXxMB=ssDJwB=vHAAlD=FcFyBD=9aT49C=DM26iC=D+Wi9B=QlgkvC=DObHjD=zcu3yB=Rri+o=IWgX6C=/p/Yn=G7+YPC=dRP7WD=JfmUi=8vq/YC=MnDt6D=2gjIPC=TE+AYB=01JcEC=fCZ7I=6NQfCC=aQhqw=AfnVJ=eCWkC=sWBYbD=s0W6uD=xyLPXB=8Z+GL=9MjGKD=zgjl/C=6bmuVC=O/nI7C=seBf3B=pDPkHB=Tm1NjB";

        String decryptedText = Rsa.decrypt(privateKey, encryptedText);
        System.out.println(decryptedText);

        Assert.assertTrue(originalText.equals(decryptedText));
    }

    @Test
    public void decryptWithServerKey() {
        String originalText = "加解密字符串hello world!逆向工程";
        String privateKey = "=-306=+Sggw=wAQAC=qkgBN=3bISGC=BEQAN=CSAAF=CCDqE=BIApE=BIo=aGKAB=5ZIQV=u4DYMB=4vVDFC=UXKnMD=O5nozB=E0V61=qSFHrB=CqIcWC=kzSOpC=Yy+hRB=pPhm7B=nrWtY=Ce7wW=RpU4rD=V0af8B=4UN7R=Sb+7aD=hGD3J=eHIzlB=Ay2CWC=cyGvDC=6tY9JC=aMJgbC=Eu5FTB=yvheYD=sfqlgD=94gSbB=10q+nB=lUA5VD=lnS3M=BMOL8C=NRdpOB=D2Ha6D=9tmLKB=CjbGCB=22XBnD=E/HdlB=0HEAw=DSJnfC=+tOeOD=ENV8p=ovoZ0C=uy7GRC=a9DSyD=O373RC=sLxEEB=SmuQvD=Cts+yD=nOUxGD=+3juAC=LekmJ=F31uV=xuKf9D=e35cj=0AEyZB=TCTbAB=mTnloB=sNzX2B=9dZ24D=NLqMbB=/x8MqB=vngjO=kWnj2D=DIQTWC=CEAAB=iBQACC=yNoeQB=tH7UM=6GYoCD=43Fil=GxpYpD=zUOUZ=x053IC=fMpQJD=LCLVBB=w/IMm=ZCI6Z=jz1YCC=yqXy3C=ofNfnC=Y4O49=4D/yhC=Q8RibD=b5gUfC=hZMZHB=UqHSwB=wguL9C=5L+nvB=L2tUDD=jIdJh=eszU0D=+TWyLD=M+eZuC=aUNQwD=qnZuR=mssWjC=eFMNVC=Slmyt=ThMF3D=OiZG=B5GIeD=2OEbEC=QONjfD=TFN8JB=jk1nX=cy3Y2=lFKkWD=NlMpfB=4d0FFD=lVUHgB=e6HlK=KFmpH=5UWwEC=wRa0a=KgM/iC=fUUsvD=+ebaWB=jhYyE=gsQOED=szAiuB=oIBf5B=SdaLYD=X769/C=NE2wcC=wH3WqC=SrOQS=CfYthC=OR/bDC=XumCnB=CEwvwB=XDQgBC=ap7/gB=HaDnlD=Q/wEnD=ZPhHaB=qi7bZ=zXx4S=9/NsrD=ks7Gk=VmaEMD=WKdumC=d6eB4D=piJZ+B=cmHzN=JmktkC=Bx5pf=rlyAwC=RsHIIB=6tnw6=2AmHDB=0LEwF=MFVw7B=Fjn92D=AvpA5D=VQ82LD=WsKr2C=Z/AO8D=M+g3uB=w5weL=dGnG2=uAKiVD=XTCtKB=CEsQzD=ADQgBC=Z+lmU=TadE5=ttY3NB=18Q66=pB5JhC=ZNdWn=fMouzD=I41MUB=E6Y3vD=SUyNg=HylM0D=GtDjz=bEa2k=ZJyRWB=+vXzjC=3AeuQB=RsezBD=FXk0QD=G5iJnC=XcXt5B=yAc4WD=FVbDvD=qYagIC=qALMJ=I32IFB=39QcIB=wt3UY=WrhwoB=GORBRD=eiHMjC=GBTQ5C=C0oUH=aCQgBC=xSRVMC=1QAAz=i++/CB=/OHeP=jfAPeC=x79hqC=zS2FYD=OQOHu=GwQjEB=se+yBC=Ja2LDB=HKE8G=dxniUB=eQ/5FC=Ba3Y8B=TSpPAD=vS5/R=jLVXJD=vtDlpB=YzscoC=4CPkID=KIfosD=FT/YF=S8twED=XdWsBD=AasBoC=ZWIy8C=Vj4h1B=oVPTGD=wlKmPB=3PJVuB=CEE7NC=6CQgBC=5EDSOC=TsFO8B=GJLuOD=ENRxwB=JIPNfC=XrscvB=dOR5+C=63mBGB=hPdH4B=FdJ+3B=2R7bFC=rMlFMC=GYtdZC=HPVEbD=ttHVEC=XansRB=DLH4w=aNh8mB=XgfMZ=pCd9yD=JgsxSC=2wLLwC=0tkkAD=51CYgD=3xPlgC=kFJBeB=Y67d2C=ytu56=CGHqgB=SEQ1+B=yx8EI=CEbg3D=EpDgBC=0kSIWC=7Ee1V=ATDiXD=bG0yOB=YfOj1B=puvdf=Q83aaD=SiuB/=flx9iC=ThnSqB=EYAN8D=2KlPU=Yy2CnB=TMZUF=BBYR/C=1bZ/GB=0j6FIC=i6GDM=jb52k=ZV3LJC=y8sGs=4sgxLB=GOLxgC=6AlP2B=oLPwV=1YkEk=4w8IhC=a++r8=HyjdwB=NhBf4B=Kr4oPB=RB=T+///D";
        String encryptedText = "=TqFGy=vXYvdC=X9iYMB=+MOtLB=e5U1C=tZpj9B=DQI4GC=POOr5B=gsbVYC=mNz3DB=5ruZEC=hP3NEC=oFXKtD=smzoPB=iKAEH=ehUcXC=LtQnoD=kfaGyD=nOn5cD=BabFOD=loJsN=73EfzD=hoSzMB=o23sN=u4hGTC=1VzEnC=ikEBYD=6EYxFB=acNTG=SMSsX=jCqruD=pEog4B=d5IIb=pCK5oC=nQIYiD=zeipt=Tq2CUC=2paEvC=U+pblC=1L5rGC=IDJS5C=9/eoKD=xAXH2D=pRY47C=7FyH1C=yLtufD=I/jl7C=OEm/SC=LWbdJ=4BKhND=lrMgy=z8Xta=jcbGzD=25VJM=tgBNdC=u+a1JC=jgYWHC=J8phoC=dqI41D=boZAJB=zPPbfB=8Q7/KC=aBBIvC=nH7JgD";

        String decryptedText = Rsa.decrypt(privateKey, encryptedText);
        System.out.println(decryptedText);

        Assert.assertTrue(originalText.equals(decryptedText));
    }

}
