import {useEffect} from 'react';
import {useDispatch, useSelector} from 'react-redux';
import {selectCategories} from '../../../store/categories/categorySelector';
import {loadCategories} from '../../../store/categories/categorySlice';
import {selectCategory} from '../../../store/filter/filterSelectors';
import {useQueryParams} from './useQueryParams';

export const useCategory = () => {
  const dispatch = useDispatch();
  const [changeQueryParams] = useQueryParams();
  const category = useSelector(selectCategory);
  const categoryList = useSelector(selectCategories);

  useEffect(() => {
    dispatch(loadCategories());
  }, [dispatch]);

  const handleSelect = e => {
    changeQueryParams(e.target.name, e.target.value);
  };

  return {category, categoryList, handleSelect};
};
