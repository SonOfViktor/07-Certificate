import {useRef} from 'react';
import CategoryList from '../components/main/CategoryList';
import CertificateSection from '../components/main/CertificateSection';

const MainPage = () => {
  const upScrollElement = useRef();

  return (
    <main>
      <CategoryList ref={upScrollElement} />
      <CertificateSection upScrollElement={upScrollElement} />
    </main>
  );
};

export default MainPage;
