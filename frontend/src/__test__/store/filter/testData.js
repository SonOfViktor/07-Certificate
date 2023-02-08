export const createFilterState = (
  sort,
  search,
  description,
  tags,
  category
) => ({
  sort,
  search,
  description,
  tags,
  category,
});

export const filterState = createFilterState(
  {field: 'name', order: 'DESC'},
  'search',
  'description',
  ['tag1', 'tag2'],
  'Sport'
);
