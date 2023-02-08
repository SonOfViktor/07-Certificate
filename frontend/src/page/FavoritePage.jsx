import FavoriteCertificates from '../components/favorite/FavoriteCertificates';
import FormHat from '../components/ui/FormHat';

const FavoritePage = () => {
  return (
    <main>
      <FormHat title="Favorite" />
      <FavoriteCertificates />
    </main>
  );
};

export default FavoritePage;
