import {useSelector} from 'react-redux';
import {selectCertificatesInfo} from '../../store/certificates/certificateSelectors';
import LoadingAlert from '../ui/LoadingAlert';
import LoadingData from '../ui/LoadingData';
import CouponList from './CouponList';
import UpScrollButton from './UpScrollButton';

const CertificateSection = ({upScrollElement}) => {
  const {status, error} = useSelector(selectCertificatesInfo);

  return (
    <>
      <LoadingAlert status={status} error={error} />
      <CouponList />
      <LoadingData status={status} />
      <UpScrollButton upScrollElement={upScrollElement} />
    </>
  );
};

export default CertificateSection;
